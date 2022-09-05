package com.afba.imageplus.service.impl;

import com.afba.imageplus.annotation.DocumentTypeAuthorization;
import com.afba.imageplus.annotation.QueueAuthorization;
import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.repository.BaseRepository;
import com.afba.imageplus.service.BaseService;
import com.afba.imageplus.service.ErrorService;
import com.afba.imageplus.utilities.AuthorizationHelper;
import com.afba.imageplus.utilities.BeanUtilsWrapper;
import com.afba.imageplus.utilities.PropertyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

// Todo: implement logging
public abstract class BaseServiceImpl<E, ID> implements BaseService<E, ID> {

    protected BaseRepository<E, ID> repository;
    private EntityManager entityManager;
    private Class<E> entityClass;

    @Autowired
    protected ApplicationContext applicationContext;
    @Autowired
    protected ErrorService errorService;
    @Autowired
    protected AuthorizationHelper authorizationHelper;

    protected BaseServiceImpl(BaseRepository<E, ID> repository) {
        this.repository = repository;
    }

    @Override
    public Optional<E> findById(ID id) {
        var optionalEntity = repository.findById(id);
        optionalEntity.ifPresent(e -> authorizationHelper.authorizeEntity("F", e));
        return optionalEntity;
    }

    @Override
    public Page<E> findAll(Pageable pageable, Map<String, Object> filters) {
        // Extract EntityManager bean
        var em = getEntityManager();
        // Get Metamodel of entity class
        var entityType = em.getMetamodel().entity(getEntityClass());
        // Create criteria query builder
        CriteriaBuilder builder = em.getCriteriaBuilder();
        // Create data & count criteria query for the given entity class
        CriteriaQuery<E> criteria = builder.createQuery(getEntityClass());
        CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
        // Create FROM for both query to entity class
        Root<E> root = criteria.from(getEntityClass());
        Root<E> countRoot = countCriteria.from(getEntityClass());
        // Initialize predicate arrays for filled with filter criteria for both queries
        var predicates = new ArrayList<Predicate>();
        var countPredicates = new ArrayList<Predicate>();
        // For each query param
        for (var filter : filters.entrySet()) {
            // Add filter to both predicate lists
            try {
                predicates.add(buildPredicate(builder, root, filter.getKey(), filter.getValue()));
                countPredicates.add(buildPredicate(builder, countRoot, filter.getKey(), filter.getValue()));
            } catch (IllegalArgumentException ignored) {
                // If the filter key does not match any attribute in the entity, skip that
                // filter.
            }
        }
        // Add authorization predicates
        predicates.addAll(getAuthorizationPredicates(builder, root));
        countPredicates.addAll(getAuthorizationPredicates(builder, countRoot));
        // Add predicates to respective queries
        criteria.where(predicates.toArray(Predicate[]::new));
        countCriteria.where(countPredicates.toArray(Predicate[]::new));
        // Run count query to get total no of records based on filter criteria received
        var count = em.createQuery(countCriteria.select(builder.count(countRoot))).getSingleResult();
        // Set order by clause of data query if received in request
        criteria.orderBy(QueryUtils.toOrders(pageable.getSort(), root, builder));
        // Also fetch none collection associations
        for (var singularAttribute : entityType.getSingularAttributes()) {
            if (singularAttribute.isAssociation()) {
                root.fetch(singularAttribute, JoinType.LEFT);
            }
        }
        // Create executable query from criteria query
        var query = em.createQuery(criteria.select(root));
        // Set pagination parameters to the query
        if (pageable.isPaged()) {
            query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            query.setMaxResults(pageable.getPageSize());
        }
        // Execute query to get data in list
        var result = query.getResultList();
        // Initialize page object with queried data and return
        return new PageImpl<>(result, pageable, count);
    }

    @Transactional
    @Override
    public E save(E entity) {
        return repository.save(entity);
    }

    @Transactional
    @Override
    public E insert(E entity) {
        var newId = getNewId(entity);
        if (newId != null && repository.existsById(newId)) {
            return errorService.throwException(HttpStatus.CONFLICT,
                    getEKDError(String.valueOf(HttpStatus.CONFLICT.value())), newId);
        }
        setIdInEntity(newId, entity);
        authorizationHelper.authorizeEntity("I", entity);
        return save(entity);
    }

    @Transactional
    @Override
    public E update(ID id, E entity) {
        var existingEntityNullable = repository.findById(id);
        if (existingEntityNullable.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND,
                    getEKDError(String.valueOf(HttpStatus.NOT_FOUND.value())), id);
        }
        var existingEntity = existingEntityNullable.get();
        try {
            BeanUtilsWrapper.getInstance().copyProperties(existingEntity, entity);
        } catch (IllegalAccessException | InvocationTargetException ignore) {
            // Ignored because same class on both side should never throw this exception
        }
        setIdInEntity(id, existingEntity);
        authorizationHelper.authorizeEntity("U", existingEntity);
        return save(existingEntity);
    }

    @Transactional
    @Override
    public void delete(ID id) {
        var optionalEntity = repository.findById(id);
        if (optionalEntity.isEmpty()) {
            errorService.throwException(HttpStatus.NOT_FOUND, getEKDError(String.valueOf(HttpStatus.NOT_FOUND.value())),
                    id);
            return;
        }
        authorizationHelper.authorizeEntity("D", optionalEntity.get());
        repository.delete(optionalEntity.get());
    }

    @Override
    public boolean existsById(ID id) {
        return repository.existsById(id);
    }

    protected abstract ID getNewId(E entity);

    private void setIdInEntity(ID id, E entity) {
        // If entity has composite key
        if (entity.getClass().isAnnotationPresent(IdClass.class)) {
            // Copy all id properties from id object to respective id properties in entity
            BeanUtils.copyProperties(id, entity);
        } else {
            // For each property of entity class
            for (var field : entity.getClass().getDeclaredFields()) {
                // Check if the property is ID property
                if (field.getAnnotation(Id.class) != null) {
                    // Set value to the ID property by calling setter method
                    var property = BeanUtils.getPropertyDescriptor(entity.getClass(), field.getName());
                    if (property == null) {
                        throw new DomainException(HttpStatus.INTERNAL_SERVER_ERROR, EKDError.EKD000500.code(),
                                String.format("Invalid entity configuration for class %s.", entity.getClass()));
                    }
                    PropertyUtil.setPropertyValue(property, entity, id);
                    break;
                }
            }
        }
    }

    protected EntityManager getEntityManager() {
        if (entityManager == null) {
            entityManager = applicationContext.getBean(EntityManager.class);
        }
        return entityManager;
    }

    protected Class<E> getEntityClass() {
        if (entityClass == null) {
            entityClass = (Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                    .getActualTypeArguments()[0];
        }
        return entityClass;
    }

    public EKDError getEKDError(String suffix) {
        var infix = getEntityClass().getAnnotation(Table.class).name().replace("EKD0", "");
        try {
            return EKDError.valueOf("EKD" + infix + suffix);
        } catch (IllegalArgumentException ignored) {
            return EKDError.valueOf(("EKD000" + suffix));
        }

    }

    public Predicate buildPredicate(CriteriaBuilder builder, Root<?> root, String attribute, Object value) {
        var attributeSplit = attribute.split("_");
        var expression = getRecursiveAttribute(root, attributeSplit[0]);
        Predicate predicate;
        if (attributeSplit.length == 1) {
            predicate = builder.equal(expression, getAttributeValue(expression, value));
        } else {
            if ("in".equals(attributeSplit[1])) {
                predicate = expression.in(Arrays.stream(String.valueOf(value).split(","))
                        .map(s -> getAttributeValue(expression, s.trim())).collect(Collectors.toList()));
            } else if ("ne".equals(attributeSplit[1])) {
                predicate = builder.notEqual(expression, getAttributeValue(expression, value));
            } else {
                predicate = builder.equal(expression, getAttributeValue(expression, value));
            }
        }
        return predicate;
    }

    private Expression<?> getRecursiveAttribute(Root<?> root, String fullAttribute) {
        var attributes = fullAttribute.split("\\.");
        Path<?> path = root;
        for (var attribute : attributes) {
            path = path.get(attribute);
        }
        return path;
    }

    private Object getAttributeValue(Expression<?> expression, Object value) {
        if (expression.getJavaType().isEnum()) {
            try {
                return Enum.valueOf((Class) expression.getJavaType(), value.toString());
            } catch (IllegalArgumentException ignore) {
                return null;
            }
        } else {
            return value;
        }
    }

    public List<Predicate> getAuthorizationPredicates(CriteriaBuilder builder, Root<?> root) {
        var predicates = new ArrayList<Predicate>();
        // Conclude authorization for queue ids
        var queueIds = authorizationHelper.getAuthorizedQueueIds();
        if (queueIds != null) {
            Arrays.stream(getEntityClass().getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(QueueAuthorization.class)
                            && field.getAnnotation(QueueAuthorization.class).findAll())
                    .forEach(field -> {
                        if (field.getAnnotation(QueueAuthorization.class).allowIfNull()) {
                            predicates.add(builder.or(builder.isNull(root.get(field.getName())),
                                    root.get(field.getName()).in(queueIds)));
                        } else {
                            predicates.add(root.get(field.getName()).in(queueIds));
                        }
                    });
        }
        // Conclude authorization for document type ids
        var documentTypeIds = authorizationHelper.getAuthorizedDocumentTypeIds();
        if (documentTypeIds != null) {
            Arrays.stream(getEntityClass().getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(DocumentTypeAuthorization.class)
                            && field.getAnnotation(DocumentTypeAuthorization.class).findAll())
                    .forEach(field -> {
                        if (field.getAnnotation(DocumentTypeAuthorization.class).allowIfNull()) {
                            predicates.add(builder.or(builder.isNull(root.get(field.getName())),
                                    root.get(field.getName()).in(documentTypeIds)));
                        } else {
                            predicates.add(root.get(field.getName()).in(documentTypeIds));
                        }
                    });
        }
        return predicates;
    }

    public E findByIdOrElseThrow(ID id) {
        var entityOpt = findById(id);
        if (entityOpt.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND,
                    getEKDError(String.valueOf(HttpStatus.NOT_FOUND.value())), id);
        }
        return entityOpt.get();
    }
}
