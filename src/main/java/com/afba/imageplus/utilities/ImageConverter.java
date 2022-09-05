package com.afba.imageplus.utilities;

import com.afba.imageplus.constants.ApplicationConstants;
import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.constants.ErrorConstants;
import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.dto.res.CaseCommentLineRes;
import com.afba.imageplus.dto.res.CaseCommentRes;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Component
public class ImageConverter {

    private final Logger logger = LoggerFactory.getLogger(ImageConverter.class);

    private final AsyncTaskExecutor conversionTaskExecutor;

    public ImageConverter(AsyncTaskExecutor conversionTaskExecutor) {
        this.conversionTaskExecutor = conversionTaskExecutor;
    }

    /**
     * Takes TIFF document as byte array, converts each page to byte array and put
     * them into PDF document. Finally, returns PDF document as byte array output
     * stream.
     *
     * @param tiffImage Tiff images input stream.
     * @return PDF Document as ByteArrayOutputStream.
     * @throws IOException When input stream throws IOException
     */
    public ByteArrayOutputStream convertTiffToPdf(final byte[] tiffImage) throws IOException {
        return convertTiffToPdf(new ByteArrayInputStream(tiffImage));
    }

    /**
     * Takes TIFF document as input stream, converts each page to byte array and put
     * them into PDF document. Finally, returns PDF document as byte array output
     * stream.
     *
     * @param tiffImage Tiff images input stream.
     * @return PDF Document as ByteArrayOutputStream.
     * @throws IOException When input stream throws IOException
     */
    public ByteArrayOutputStream convertTiffToPdf(final InputStream tiffImage) throws IOException {
        // Retrieve TIFF image reader from ImageIO registry.
        var tiffReader = ImageIO.getImageReadersByFormatName("tif").next();
        // Set TIFF image input stream to the TIFF reader.
        tiffReader.setInput(ImageIO.createImageInputStream(tiffImage));
        // Initialize byte array stream to return.
        var pdfStream = new ByteArrayOutputStream();
        // Initialize an empty PDF document.
        try (var pdDocument = new PDDocument()) {

            // Extract pages unless there are no pages left and add them to PDF document.
            extractAndAddPagesToPDF(tiffReader, pdDocument);

            // Write PDF document to byte array output stream. If there are no pages in TIFF
            // document, empty PDF will be
            // returned.
            pdDocument.save(pdfStream);
        } catch (Exception ex) {
            logger.error(ErrorConstants.ERROR_WHILE_CONVERTING, ex.getMessage());
            throw new DomainException(HttpStatus.BAD_GATEWAY, ErrorConstants.IMAGE_CONVERSION_FAILED);
        }
        return pdfStream;
    }

    private void extractAndAddPagesToPDF(ImageReader tiffReader, PDDocument pdDocument) throws IOException {
        // Declare and initialize helper variables to extract all pages from TIFF
        // document.
        var hasNextPage = true;
        var i = 0;
        // Extract pages unless there are no pages left and add them to PDF document.
        while (hasNextPage) {
            try {
                // Read next page from TIFF document.
                var bufferedImage = tiffReader.read(i);
                // Convert image to PDF page.
                var pageEntry = convert(i, bufferedImage, pdDocument, "tif");
                // Add PDF page to the PDF document.
                pdDocument.addPage(pageEntry.getValue());
                // Increment to get next page from TIFF document.
                i++;
            } catch (IndexOutOfBoundsException ex) {
                // If there are no page found on the given index then EOF has reached.
                hasNextPage = false;
            }
        }
    }

    /**
     * Takes TIFF document as byte array, converts each page to byte array and put
     * them into PDF document. Finally, returns PDF document as byte array output
     * stream.
     *
     * @param tiffImage Tiff image input stream.
     * @return PDF document as ByteArrayOutputStream.
     * @throws IOException When input stream throws IOException
     */
    public ByteArrayOutputStream convertTiffToPdfThreaded(final byte[] tiffImage) throws IOException {
        return convertTiffToPdfThreaded(new ByteArrayInputStream(tiffImage));
    }

    /**
     * Takes TIFF document as input stream, converts each page to byte array and put
     * them into PDF document. Finally, returns PDF document as byte array output
     * stream.
     *
     * @param tiffImage Tiff images input stream.
     * @return PDF document as ByteArrayOutputStream.
     * @throws IOException When input stream throws IOException
     */
    public ByteArrayOutputStream convertTiffToPdfThreaded(final InputStream tiffImage) throws IOException {
        // Retrieve TIFF image reader from ImageIO registry.
        var tiffReader = ImageIO.getImageReadersByFormatName("tif").next();
        // Set TIFF image input stream to the TIFF reader.
        tiffReader.setInput(ImageIO.createImageInputStream(tiffImage));
        // Initialize byte array stream to return.
        var pdfStream = new ByteArrayOutputStream();
        // Initialize an empty PDF document.
        try (var pdDocument = new PDDocument()) {
            // Call the image converter.
            convertImageToPdf(tiffReader, pdDocument, "tif");
            // Write PDF document to byte array output stream. If there are no pages in TIFF
            // document, empty PDF will be
            // returned.
            pdDocument.save(pdfStream);
        } catch (Exception ex) {
            logger.error(ErrorConstants.ERROR_WHILE_CONVERTING, ex.getMessage());
            throw new DomainException(HttpStatus.BAD_GATEWAY, ErrorConstants.IMAGE_CONVERSION_FAILED);
        }
        return pdfStream;
    }

    /**
     * Takes TIFF document as byte array, converts each page to byte array and put
     * them into PDF document. Finally, returns PDF document as byte array output
     * stream.
     *
     * @param tiffImages Tiff images input stream.
     * @return PDF document as ByteArrayOutputStream.
     * @throws IOException When input stream throws IOException
     */
    public ByteArrayOutputStream convertTiffsToPdfThreaded(final List<byte[]> tiffImages) throws IOException {
        return convertTiffsToPdfThreaded(
                tiffImages.stream().map(ByteArrayInputStream::new).toArray(ByteArrayInputStream[]::new));
    }

    /**
     * Takes TIFF document as input stream, converts each page to byte array and put
     * them into PDF document. Finally, returns PDF document as byte array output
     * stream.
     *
     * @param tiffImages Tiff images input stream.
     * @return PDF document as ByteArrayOutputStream.
     * @throws IOException When input stream throws IOException
     */
    public ByteArrayOutputStream convertTiffsToPdfThreaded(final InputStream[] tiffImages) throws IOException {
        // Retrieve TIFF image reader from ImageIO registry.
        var tiffReader = ImageIO.getImageReadersByFormatName("tif").next();
        // Initialize byte array stream to return.
        var pdfStream = new ByteArrayOutputStream();
        // Initialize an empty PDF document.
        try (var pdDocument = new PDDocument()) {
            for (var tiffImage : tiffImages) {
                // Set TIFF image input stream to the TIFF reader.
                tiffReader.setInput(ImageIO.createImageInputStream(tiffImage));
                // Call the image converter.
                convertImageToPdf(tiffReader, pdDocument, "tif");
            }
            // Write PDF document to byte array output stream. If there are no pages in TIFF
            // document, empty PDF will be
            // returned.
            pdDocument.save(pdfStream);
        } catch (Exception ex) {
            logger.error(ErrorConstants.ERROR_WHILE_CONVERTING, ex.getMessage());
            throw new DomainException(HttpStatus.BAD_GATEWAY, ErrorConstants.IMAGE_CONVERSION_FAILED);
        }
        return pdfStream;
    }

    // Calculates scale to which image should be resized to fit PDF page.
    private float getResizeScale(final PDPage page, final PDImageXObject image) {
        // The default scale will be 1. If height and width of image is within PDF page
        // size, scale stays 1.
        float scale = 1;
        // If image width is greater than page width:
        if (image.getWidth() > page.getMediaBox().getWidth()) {
            // Divide page width with image width to set scale.
            scale = page.getMediaBox().getWidth() / image.getWidth();
        }
        // if the scaled image height is still greater than page height:
        if ((image.getHeight() * scale) > page.getMediaBox().getHeight()) {
            // Recalculate the scale by dividing page height with scaled image height.
            scale = page.getMediaBox().getHeight() / (image.getHeight() * scale);
        }
        return scale;
    }

    /**
     * Reads multiple pages from image reader and puts into PDF document as pages.
     *
     * @param imageReader Image reader to read multiple page of image from.
     * @param pdDocument  PDF document to write the converted image to.
     * @throws IOException
     */
    private void convertImageToPdf(ImageReader imageReader, PDDocument pdDocument, String format) throws IOException {
        var pages = new TreeMap<Integer, PDPage>();
        // Extract pages unless there are no pages left and add them to PDF document.
        var futurePages = new ArrayList<Future<Map.Entry<Integer, PDPage>>>();
        // Declare and initialize helper variables to extract all pages from TIFF
        // document.
        var hasNextPage = true;
        var i = 0;
        // Extract pages unless there are no pages left and add them to PDF document.
        while (hasNextPage) {
            try {
                // Read next page from TIFF document.
                var bufferedImage = imageReader.read(i);
                // Add conversion task to queue to convert tiff image to PDF page.
                var pageNo = i + 1;
                futurePages
                        .add(conversionTaskExecutor.submit(() -> convert(pageNo, bufferedImage, pdDocument, format)));
                logger.debug("Initiated conversion request for page no {}", pageNo);
                // Increment to get next page from TIFF document.
                i++;
            } catch (IndexOutOfBoundsException ex) {
                // If there are no page found on the given index then EOF has reached.
                hasNextPage = false;
            }
        }
        // Wait for each thread to complete before adding pages to PDF document.
        futurePages.forEach(fp -> {
            try {
                var pageEntry = fp.get();
                logger.debug("Completed conversion request for page no {}", pageEntry.getKey());
                pages.put(pageEntry.getKey(), pageEntry.getValue());
            } catch (InterruptedException | ExecutionException e) {
                logger.error(ErrorConstants.ERROR_WHILE_CONVERTING, e.getMessage());
                Thread.currentThread().interrupt();
                throw new DomainException(HttpStatus.BAD_GATEWAY, ErrorConstants.IMAGE_CONVERSION_FAILED);
            }
        });
        // Add all pages to PDF document.
        pages.values().forEach(pdDocument::addPage);
    }

    // Converts buffered image to byte array and put into a PDF page then returns
    // the PDF page.
    private Map.Entry<Integer, PDPage> convert(int pageNo, BufferedImage bufferedImage, PDDocument pdDocument,
            String format) throws IOException {
        // Initialize a byte array output stream.
        var byteArrayOutputStream = new ByteArrayOutputStream();
        // Convert TIFF's current page image to output stream.
        ImageIO.write(bufferedImage, format, byteArrayOutputStream);
        // Initialize a new PDF page.
        var pdPage = new PDPage();
        // Create ImageX Object to embed in PDF page.
        var pdImage = PDImageXObject.createFromByteArray(pdDocument, byteArrayOutputStream.toByteArray(), null);
        var contentStream = new PDPageContentStream(pdDocument, pdPage, PDPageContentStream.AppendMode.APPEND, true,
                true);
        // Get resize scale to make the image fit into PDF page.
        // Will return 1 if image size is already within PDF page size.
        var scale = getResizeScale(pdPage, pdImage);
        // Calculate resized width and height of the image.
        var width = pdImage.getWidth() * scale;
        var height = pdImage.getHeight() * scale;
        // Calculate x and y on PDF page to center the image.
        var x = (pdPage.getMediaBox().getWidth() / 2) - (width / 2);
        var y = (pdPage.getMediaBox().getHeight() / 2) - height + (height / 2);
        // Draw image to the PDF page and close the content stream.
        contentStream.drawImage(pdImage, x, y, width, height);
        contentStream.close();
        return Map.entry(pageNo, pdPage);
    }

    public void prepareDataForCommentTiff(List<CaseCommentRes> caseComments) {
        for (CaseCommentRes caseComment : caseComments) {
            var lines = new ArrayList<>(new LinkedHashSet<>(caseComment.getCommentLines()));
            for (int k = 0; k < lines.size(); k++) {
                if (lines.get(k).getCommentLine().length() > 65) {
                    String[] commentsWithLineBreak =
                            addLineBreaks(lines.get(k).getCommentLine(), 64).split("\\r?\\n");
                    lines.remove(k);
                    populateCollectionWithNewLines(commentsWithLineBreak, lines, k);
                    caseComment.setCommentLines(new LinkedHashSet<>(lines));
                }
            }
        }
    }

    public String generateTiffFromCommentList(String directoryDescriptor, List<CaseCommentRes> caseComments, String policyId) {
        final String imagePath = directoryDescriptor + File.separator;
        try {
            prepareDataForCommentTiff(caseComments);
            final int maxPixelMoveAlongY = 30;
            int page = 1;
            int pageHeightUsed = 0;
            int k = 0;
            boolean isNewPage = true;
            BufferedImage myPicture = null;
            Graphics graphics = null;
            int i = 0;
            Files.createDirectories(Paths.get(imagePath));
            for (; i < caseComments.size(); i++) {
                if (isNewPage) {
                    myPicture = new BufferedImage(
                            ApplicationConstants.COMMENTS_TIFF_MAX_WIDTH,
                            ApplicationConstants.COMMENTS_TIFF_MAX_HEIGHT,
                            BufferedImage.TYPE_INT_RGB
                    );
                    graphics = myPicture.getGraphics();
                    setImageHeader(myPicture, graphics, maxPixelMoveAlongY, page, policyId);
                    isNewPage = false;
                    pageHeightUsed = (3 * maxPixelMoveAlongY) + maxPixelMoveAlongY;
                }
                var lines = new ArrayList<>(caseComments.get(i).getCommentLines());
                while (k < lines.size()) {
                    if (pageHeightUsed <=
                            (ApplicationConstants.COMMENTS_TIFF_MAX_HEIGHT - ((lines.size() - 1) * maxPixelMoveAlongY))
                    ) {
                        pageHeightUsed = addCommentorDetailsToImage(
                                graphics,
                                caseComments.get(i).getUserId(),
                                caseComments.get(i).getCommentDate(),
                                caseComments.get(i).getCommentTime(),
                                pageHeightUsed, k, maxPixelMoveAlongY
                        );
                        graphics.setFont(new Font(ApplicationConstants.FONT_FAMILY, Font.PLAIN, 20));
                        graphics.drawString(lines.get(k).getCommentLine(), 30, pageHeightUsed);
                        k++;
                        pageHeightUsed += maxPixelMoveAlongY;
                    } else {
                        i--;
                        page = createNewImagePage(true, myPicture, imagePath, page);
                        isNewPage = true;
                        break;
                    }
                }
                // This will aid in iterating the loop over comment lines, when the content limit of the page is available to populate more comments
                // No need to make a new page here
                k = shouldRestartCommentLineIndex(!isNewPage, k);
                // This condition ensures that all comments have been added before we have reached the content limit, so write to tiff
                page = createNewImagePage((!isNewPage && caseComments.size() - i == 1), myPicture, imagePath, page);
            }

            return imagePath;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DomainException(HttpStatus.INTERNAL_SERVER_ERROR, EKDError.EKD000500.code(), "Something went wrong while generating tiff for comments.");
        }
    }

    public String createMultiPageTiff(final String imagePath) {
        try {

            File sourceDirectory = new File(imagePath);
            File[] file = sourceDirectory.listFiles();

            assert file != null;
            Arrays.sort(file, Comparator.comparing(File::getName));
            BufferedImage[] images = new BufferedImage[file.length];

            for (var i = 0; i < images.length; i++) {
                images[i] = ImageIO.read(new File(file[i].getPath()));
            }
            OutputStream stream = new FileOutputStream(imagePath + "combined.tiff");

            // Obtain a TIFF writer
            ImageWriter writer = ImageIO.getImageWritersByFormatName("TIFF").next();

            try (ImageOutputStream output = ImageIO.createImageOutputStream(stream)) {
                writer.setOutput(output);

                ImageWriteParam params = writer.getDefaultWriteParam();
                params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);

                // Compression: None, PackBits, ZLib, Deflate, LZW, JPEG and CCITT variants allowed
                // (different plugins may use a different set of compression type names)
                params.setCompressionType("Deflate");

                writer.prepareWriteSequence(null);

                for (BufferedImage image : images) {
                    writer.writeToSequence(new IIOImage(image, null, null), params);
                }
                // We're done
                writer.endWriteSequence();
            }
            stream.close();
            writer.dispose();
            return imagePath + "combined.tiff";
        } catch (Exception e) {
                e.printStackTrace();
                throw new DomainException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong while generating tiff for comments.");
        }
    }

    private void setImageHeader(BufferedImage myPicture, Graphics graphics, int maxPixelMoveAlongY, int page, String policyId) {
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, ApplicationConstants.COMMENTS_TIFF_MAX_WIDTH, ApplicationConstants.COMMENTS_TIFF_MAX_HEIGHT);

        graphics.setColor(Color.BLACK);

        graphics.setFont(new Font(ApplicationConstants.FONT_FAMILY, Font.BOLD, 20));
        graphics.drawString("F11 Case Comments", (myPicture.getWidth() / 2) - 90, maxPixelMoveAlongY);

        graphics.setFont(new Font(ApplicationConstants.FONT_FAMILY, Font.BOLD, 15));

        graphics.drawString("AFBA # ".concat(policyId), 25, (2 * maxPixelMoveAlongY));
        graphics.drawString("Page # " + page, ApplicationConstants.COMMENTS_TIFF_MAX_WIDTH - 100, (2 * maxPixelMoveAlongY));
    }

    public String addLineBreaks(String input, int maxLineLength) {
        StringTokenizer tok = new StringTokenizer(input, " ");
        StringBuilder output = new StringBuilder(input.length());
        int lineLen = 0;
        while (tok.hasMoreTokens()) {
            String word = tok.nextToken() + " ";

            if (lineLen + word.length() > maxLineLength) {
                output.append("\n");
                lineLen = 0;
            }
            output.append(word);
            lineLen += word.length();
        }
        return output.toString();
    }

    private void populateCollectionWithNewLines(String[] commentSplit, List<CaseCommentLineRes> commentLineList, Integer initialBigCommentPos) {
        var i = 1;
        for (String str: commentSplit) {
            var obj = new CaseCommentLineRes();

            obj.setCommentSequence(i++);
            obj.setCommentLine(str.trim());
            commentLineList.add(initialBigCommentPos++, obj);
        }
        updateCommentSequence(commentLineList);
    }

    private int addCommentorDetailsToImage(Graphics graphics, String user,
                                           LocalDate date, LocalTime time,
                                           int sizeIndex, final int lineIndex, final int maxPixelMoveAlongY) {
        if (lineIndex == 0) {
            graphics.setFont(new Font(ApplicationConstants.FONT_FAMILY, Font.BOLD, 20));
            graphics.drawString("User: " + user + "     Date: " + date + "     Time: " + time, 25, sizeIndex);
            sizeIndex +=maxPixelMoveAlongY;
        }
        return sizeIndex;
    }

    private int createNewImagePage(boolean shouldCreate, BufferedImage image, final String imagePath, int page) throws IOException {
        if (shouldCreate) {
            ImageIO.write(image, "TIFF", new File(imagePath + "file_" + page++ + ".tiff"));
        }
        return page;
    }

    private int shouldRestartCommentLineIndex(boolean shouldRestartIndex, int indexToRestart) {
        return shouldRestartIndex ? 0 : indexToRestart;
    }

    private void updateCommentSequence(List<CaseCommentLineRes> commentLineList) {
        for (var i = 0; i < commentLineList.size(); i++ ) {
            commentLineList.get(i).setCommentSequence(i + 1);
        }
    }
}
