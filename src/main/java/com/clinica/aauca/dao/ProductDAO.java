package com.clinica.aauca.dao;

import com.clinica.aauca.model.Product;
import java.util.List;

public interface ProductDAO {
    List<Product> findAll();
    boolean updateStock(int id, int newStock);
}
