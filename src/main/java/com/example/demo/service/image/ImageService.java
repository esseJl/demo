package com.example.demo.service.image;

import com.example.demo.model.image.Image;
import com.example.demo.model.product.Product;
import com.example.demo.model.repository.image.ImageRepository;
import com.example.demo.service.image.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageService {

    final String uploadDir = "uploads";
    final private String currentPath = System.getProperty("user.dir");

    private ImageRepository imageRepository;


    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;

    }

    public Image saveImage(Image image) {
        return imageRepository.save(image);
    }

    public List<Image> findAllByProduct(Product product) {
        return imageRepository.findAllByProduct(product);
    }


    public boolean saveNewImage2product(MultipartFile multipartFile, Product product) {

        String contentType = multipartFile.getContentType();
        System.out.println("contentType: " + contentType);
        if (multipartFile.isEmpty())
            throw new FileStorageException("multipartFile null");
        if (product == null)
            throw new FileStorageException("product null");

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String imageId = UUID.randomUUID().toString();

        try {


            Path copyLocation = Paths.get(
                    uploadDir + File.separator + imageId + File.separator
                            + fileName
            );
            Files.copy(multipartFile.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
            Image image = new Image();
            image.setImageName(fileName);
            image.setImageUUID(imageId);
            image.setProduct(product);
            imageRepository.save(image);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileStorageException("Could not store file " + multipartFile.getOriginalFilename()
                    + ". Please try again!");


        }

    }

    public Image saveNewImage(MultipartFile multipartFile) {

        String contentType = multipartFile.getContentType();
        System.out.println("contentType: " + contentType);
        if (multipartFile.isEmpty())
            throw new FileStorageException("multipartFile null");

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String imageUUID = UUID.randomUUID().toString();
        String path = uploadDir + File.separator + imageUUID + File.separator + fileName;
        try {

            if (!Files.exists(Paths.get(path))) {
                File pathAsFile = new File(path);
                pathAsFile.mkdirs();
            }
            Path copyLocation = Paths.get(path);

            Files.copy(multipartFile.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

            Image image = new Image();
            image.setImageName(fileName);
            image.setImageUUID(imageUUID);
            return image;
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileStorageException("Could not store file " + multipartFile.getOriginalFilename()
                    + ". Please try again!");

        }

    }

    public Image getImageE(String imageUUID) {
        Optional<Image> imageByImageUUID = imageRepository.getImageByImageUUID(imageUUID);
        imageByImageUUID.orElseThrow();
        return imageByImageUUID.get();
    }

    public InputStream getImage(String imageUUID) throws IOException {
        Optional<Image> image = imageRepository.getImageByImageUUID(imageUUID);
        image.orElseThrow();

        String path = File.separator + uploadDir + File.separator +
                image.get().getImageUUID() + File.separator + image.get().getImageName();

        //ServletContextResource resource = new ServletContextResource(servletContext, path);
        //InputStream in = resource.getInputStream();
        //String realPath = this.servletContext.getRealPath(path);
        //System.out.println(path);
        //URL url = this.getClass().getResource(path);
        //--------------------------------------
        /*
        String s = ServletUriComponentsBuilder.fromCurrentContextPath().path(uploadDir)
                .path(image.get().getImageName()).toUriString();
        System.out.println(s);

         */
        //--------------------------------------

        //uploads/5989220f-2ed3-4141-9469-192f40cadeae/img.jpeg
        // System.out.println(path);
        // System.out.println(System.getProperty("user.dir"));

        File file = new File(currentPath + File.separator + path);
        if (!file.exists())
            throw new FileNotFoundException();

        if (!file.isFile())
            throw new FileStorageException("path is not a file");

        //this.servletContext.getContextPath();
        // = servletContext.getResourceAsStream(path);

        return new FileInputStream(file);
    }

    public Image updateImage(String imageUUID, MultipartFile file) throws IOException {
        Optional<Image> imageByImageUUID = imageRepository.getImageByImageUUID(imageUUID);
        imageByImageUUID.orElseThrow();
        String deletePath = currentPath + File.separator + uploadDir +
                File.separator + imageByImageUUID.get().getImageUUID() +
                File.separator + imageByImageUUID.get().getImageName();

        File deleteFile = new File(deletePath);
        if (!deleteFile.isFile())
            throw new FileStorageException("path is not a file");
        if (!deleteFile.exists())
            throw new FileNotFoundException();

        boolean delete = deleteFile.delete();
        if (!delete)
            throw new FileStorageException("Can not delete file");

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        String newImagePath = currentPath + File.separator + uploadDir +
                File.separator + imageByImageUUID.get().getImageUUID() +
                File.separator + fileName;

        Path copyLocation = Paths.get(newImagePath);


        Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
        imageByImageUUID.get().setImageName(fileName);
        imageRepository.save(imageByImageUUID.get());

        return imageByImageUUID.get();
    }

    public void deleteImage(String imageUUID) throws FileNotFoundException {

        Image imageByImageUUID = getImageE(imageUUID);
        String deleteFilePath = currentPath + File.separator + uploadDir +
                File.separator + imageByImageUUID.getImageUUID() +
                File.separator + imageByImageUUID.getImageName();

        String deleteDirPath = currentPath + File.separator + uploadDir +
                File.separator + imageByImageUUID.getImageUUID();

        File deleteFile = new File(deleteFilePath);
        if (!deleteFile.isFile())
            throw new FileStorageException("path is not a file");
        if (!deleteFile.exists())
            throw new FileNotFoundException();

        boolean delete = deleteFile.delete();
        if (!delete)
            throw new FileStorageException("Can not delete file");

        File deleteDirFile = new File(deleteDirPath);
        if (!deleteDirFile.isDirectory())
            throw new FileStorageException("path is not a directory");
        if (!deleteDirFile.exists())
            throw new FileNotFoundException();

        boolean deleteDir = deleteDirFile.delete();
        if (!deleteDir)
            throw new FileStorageException("Can not delete file");

        imageRepository.deleteById(imageByImageUUID.getId());
    }
}
