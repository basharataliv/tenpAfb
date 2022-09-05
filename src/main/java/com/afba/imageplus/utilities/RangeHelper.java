package com.afba.imageplus.utilities;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RangeHelper {
    public List<Integer> lowHighRange(Integer value) {
        List<Integer> array = new ArrayList<Integer>();
        if (value.toString().length() == 6) {
            array.add(Integer.valueOf(value.toString().substring(0, 3)));
            array.add(Integer.valueOf(value.toString().substring(3, 6)));
        } else if (value.toString().length() == 5) {
            array.add(Integer.valueOf(value.toString().substring(0, 2)));
            array.add(Integer.valueOf(value.toString().substring(2, 5)));
        } else if (value.toString().length() == 4) {
            array.add(Integer.valueOf(value.toString().substring(0, 1)));
            array.add(Integer.valueOf(value.toString().substring(1, 4)));
        } else {
            array.add(0);
            array.add(value);
        }
        return array;
    }
}