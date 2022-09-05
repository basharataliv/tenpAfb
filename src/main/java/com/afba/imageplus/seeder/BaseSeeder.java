package com.afba.imageplus.seeder;

import com.afba.imageplus.repository.BaseRepository;
import org.springframework.beans.BeanUtils;

import javax.annotation.PostConstruct;
import java.util.Map;

public abstract class BaseSeeder<E, ID> {

    protected final BaseRepository<E, ID> repository;
    protected abstract Map<ID, E> getEntities();
    protected abstract boolean doUpdate();

    protected BaseSeeder(BaseRepository<E, ID> repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void seed() {
        for(var entry : getEntities().entrySet()) {
            var nullableEntity = repository.findById(entry.getKey());
            if(nullableEntity.isEmpty()) {
                repository.save(entry.getValue());
            } else if(doUpdate()) {
                var entity = nullableEntity.get();
                BeanUtils.copyProperties(entry.getValue(), entity);
                repository.save(entity);
            }
        }
    }
}
