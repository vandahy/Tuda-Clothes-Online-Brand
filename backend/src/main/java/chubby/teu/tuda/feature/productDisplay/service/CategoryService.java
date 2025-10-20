package chubby.teu.tuda.feature.productDisplay.service;

import chubby.teu.tuda.feature.productDisplay.dto.CategoryDTO;
import chubby.teu.tuda.feature.productDisplay.mapper.CategoryMapper;
import chubby.teu.tuda.feature.productDisplay.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMapper categoryMapper;

    public List<CategoryDTO> getAllCategories() {
        return categoryMapper.toDTOList(categoryRepository.findAll());
    }
}
