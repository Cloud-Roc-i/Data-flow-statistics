package com.study.dao;

import com.study.domain.CategoryClickCount;
import com.study.utils.HBaseUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class CategoryClickCountDAO {
    public List<CategoryClickCount> query(String day) throws IOException {
        List<CategoryClickCount> list = new ArrayList<>();
        Map<String,Long> map = HBaseUtils.getInstance().query("category_clickcount",day);
        for(Map.Entry<String,Long> entry:map.entrySet()){
            CategoryClickCount model = new CategoryClickCount();
            model.setName(entry.getKey());
            model.setValue(entry.getValue());

            list.add(model);
        }
        return list;
    }
}
