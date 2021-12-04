package top.chenzhimeng.hr_health_check.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.FeatureDescriptor;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author M
 * @date 2021/05/08
 **/
public class UpdateUtil {
    public static String[] getNotNullFields(Object target) {
        BeanWrapper srcBean = new BeanWrapperImpl(target);
        return Arrays.stream(srcBean.getPropertyDescriptors())
                .map(PropertyDescriptor::getName)
                .filter(name -> srcBean.getPropertyValue(name) != null)
                .toArray(String[]::new);
    }
}
