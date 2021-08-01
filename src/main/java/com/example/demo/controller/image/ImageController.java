package com.example.demo.controller.image;

import com.example.demo.model.image.Image;
import com.example.demo.model.product.Product;
import com.example.demo.service.image.ImageService;
import com.example.demo.service.product.ProductService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@Controller
@RequestMapping("/image")
public class ImageController {

    private ImageService imageService;
    private ProductService productService;

    @Autowired
    public ImageController(ImageService imageService, ProductService productService) {
        this.imageService = imageService;
        this.productService = productService;
    }

    @ResponseBody
    @RequestMapping(value = "/{imageId}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public void getImageAsByteArray(HttpServletResponse response, @PathVariable("imageId") String imageUUID) throws IOException {
        InputStream in = imageService.getImage(imageUUID);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        //System.out.println(in);
        IOUtils.copy(in, response.getOutputStream());
    }

    @ResponseBody
    @RequestMapping(value = "/{imageId}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Image getImage(@PathVariable("imageId") String imageUUID) {
        return imageService.getImageE(imageUUID);
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    public String updateImage(@RequestParam("imageId") String imageUUID, @RequestParam("file") MultipartFile file) throws IOException {
        Image image = imageService.updateImage(imageUUID, file);
        //TODO provide redirectAttribute
        return "redirect:/brand/home";
    }

    @RequestMapping(path = "/product/{productUUID}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    List<Image> getImages4product(@PathVariable("productUUID") String productUUID) {

        Product product = productService.getProduct(productUUID);
        return imageService.findAllByProduct(product);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{imageId}/delete")
    public String deleteImage(@PathVariable("imageId") String imageUUID) throws FileNotFoundException {
        imageService.deleteImage(imageUUID);
        //TODO provide redirectAttribute
        return "redirect:/product/home";
    }
}
