package com.app.shopspring.service.impl;

import com.app.shopspring.exceptions.APIException;
import com.app.shopspring.exceptions.ResourceNotFoundException;
import com.app.shopspring.model.Category;
import com.app.shopspring.model.Product;
import com.app.shopspring.payLoad.ProductDTO;
import com.app.shopspring.payLoad.ProductResponse;
import com.app.shopspring.repository.CategoryRepository;
import com.app.shopspring.repository.ProductRepository;
import com.app.shopspring.service.FileService;
import com.app.shopspring.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final FileService fileService;

    @Value("${project.image}")
    private String path;

    @Autowired
    ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, ModelMapper modelMapper,
                       FileService fileService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.fileService = fileService;
    }

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category", "category ID", categoryId));

        boolean IsProductNotPresent = true;

        List<Product> products = category.getProducts();

        for (Product product : products) {
            if (product.getProductName().equals(productDTO.getProductName())) {
                IsProductNotPresent = false;
                break;
            }
        }

        if (!IsProductNotPresent) {
            Product product = modelMapper.map(productDTO, Product.class);

            product.setCategory(category);
            product.setImage("default.png");
            double specialPrice = product.getPrice() -
                    ((product.getDiscountPrice() * 0.01) * productDTO.getPrice());
            product.setSpecialPrice(specialPrice);
            Product savedProduct = productRepository.save(product);

            return modelMapper.map(savedProduct, ProductDTO.class);
        }else{
            throw new APIException("Product Already Exist");
        }
    }

        @Override
        public ProductResponse getAllProducts (Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

            Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                    ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

            Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
            Page<Product> pageProducts = productRepository.findAll(pageDetails);
            List<Product> products = pageProducts.getContent();
            List<ProductDTO> productDTOS = products.stream().map(product -> {
                return modelMapper.map(product, ProductDTO.class);
            }).toList();

            ProductResponse productResponse = new ProductResponse();
            productResponse.setContent(productDTOS);
            productResponse.setPageNumber(pageDetails.getPageNumber());
            productResponse.setPageSize(pageDetails.getPageSize());
            productResponse.setTotalElements(pageProducts.getTotalElements());
            productResponse.setTotalPages(pageProducts.getTotalPages());

            return productResponse;
        }

        @Override
        public ProductResponse searchByCategory (Long categoryId,Integer pageNumber, Integer pageSize, String sortBy, String sortOrder){
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "Category ID", categoryId));

            Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                    ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

            Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
            Page<Product> pageProducts = productRepository.findByCategoryOrderByPriceAsc(category, pageDetails);
            List<Product> products = pageProducts.getContent();

            List<ProductDTO> productDTOS = products.stream()
                    .map(product -> modelMapper.map(product, ProductDTO.class))
                    .toList();

            ProductResponse productResponse = new ProductResponse();
            productResponse.setContent(productDTOS);
            productResponse.setPageNumber(pageDetails.getPageNumber());
            productResponse.setPageSize(pageDetails.getPageSize());
            productResponse.setTotalElements(pageProducts.getTotalElements());
            productResponse.setTotalPages(pageProducts.getTotalPages());

            return productResponse;
        }

        @Override
        public ProductResponse searchByKeyword (String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder){

            Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                    ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

            Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
            Page<Product> pageProducts = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%', pageDetails);
            List<Product> products = pageProducts.getContent();

           List<ProductDTO> productDTOS = products.stream()
                    .map(product -> modelMapper.map(product, ProductDTO.class))
                    .toList();

            ProductResponse productResponse = new ProductResponse();
            productResponse.setContent(productDTOS);
            productResponse.setPageNumber(pageDetails.getPageNumber());
            productResponse.setPageSize(pageDetails.getPageSize());
            productResponse.setTotalElements(pageProducts.getTotalElements());
            productResponse.setTotalPages(pageProducts.getTotalPages());
            return productResponse;
        }

        @Override
        public ProductDTO update (Long productId, ProductDTO productDTO){

            Product savedProduct = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "Product ID", productId));

            Product product = modelMapper.map(productDTO, Product.class);

            savedProduct.setProductName(product.getProductName());
            savedProduct.setDescription(product.getDescription());
            savedProduct.setQuantity(product.getQuantity());
            savedProduct.setDiscountPrice(product.getDiscountPrice());
            savedProduct.setPrice(product.getPrice());
            savedProduct.setSpecialPrice(product.getSpecialPrice());

            Product updatedProduct = productRepository.save(savedProduct);

            return modelMapper.map(updatedProduct, ProductDTO.class);

        }

        @Override
        public ProductDTO deleteProduct (Long productId){

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "Product ID", productId));

            productRepository.delete(product);

            return modelMapper.map(product, ProductDTO.class);
        }

        @Override
        public ProductDTO updateImage (Long productId, MultipartFile image) throws IOException {

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "Product ID", productId));

            String fileName = fileService.uploadImage(path, image);

            product.setImage(fileName);

            Product updatedProduct = productRepository.save(product);

            return modelMapper.map(updatedProduct, ProductDTO.class);
        }

    }
