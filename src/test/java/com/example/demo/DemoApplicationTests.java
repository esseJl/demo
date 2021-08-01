package com.example.demo;

import com.example.demo.model.product.Product;
import com.example.demo.model.repository.product.ProductRepository;
import com.github.eloyzone.jalalicalendar.DateConverter;
import com.github.eloyzone.jalalicalendar.JalaliDate;
import com.github.eloyzone.jalalicalendar.JalaliDateFormatter;
import com.github.eloyzone.jalalicalendar.MonthPersian;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;


@SpringBootTest
class DemoApplicationTests {


    @Autowired
    private ProductRepository repository;

    @Autowired
    private DateConverter converter;

    @Test
    @Transactional
    void contextLoads() {
        Optional<Product> byId = repository.findById(1L);
        LocalDateTime updateDateTime = byId.get().getUpdateDateTime();
        System.out.println(updateDateTime);
        JalaliDate jalaliDate = converter.gregorianToJalali(updateDateTime.getYear(),
                updateDateTime.getMonth(), updateDateTime.getDayOfMonth());
        System.out.println(jalaliDate.format(new JalaliDateFormatter("yyyy/mm/dd", JalaliDateFormatter.FORMAT_IN_ENGLISH)));
        System.out.println(jalaliDate);

        // Convert Jalali date to Gregorian
        LocalDate localdate1 = converter.jalaliToGregorian(1370, 11, 28);
        LocalDate localdate2 = converter.jalaliToGregorian(1386, MonthPersian.ESFAND, 29);

        // Convert Gregorian date to Jalali
        JalaliDate jalaliDate1 = converter.gregorianToJalali(1992, 2, 17);
        JalaliDate jalaliDate2 = converter.gregorianToJalali(2019, 3, 20);

        // checking for leapyer of Jalali Date


        boolean leapyer2 = converter.gregorianToJalali(1992, 2, 17).isLeapYear();
        boolean leapyer1 = new JalaliDate(1370, 11, 28).isLeapYear();

        // Day of week
        String dayOfWeek1 = new JalaliDate(1370, 11, 28).getDayOfWeek().getStringInPersian(); // Doshanbe
        String dayOfWeek2 = new JalaliDate(1370, 11, 28).getDayOfWeek().getStringInEnglish(); // دوشنبه

        DateConverter dateConverter = new DateConverter();
        JalaliDate jalaliDate3 = dateConverter.gregorianToJalali(1992, Month.FEBRUARY, 17);
        String result = jalaliDate.format(new JalaliDateFormatter("yyyy/mm/dd", JalaliDateFormatter.FORMAT_IN_PERSIAN));
// result will be: ١٣٧٠/١١/٢٨

        String result2 = jalaliDate.format(new JalaliDateFormatter("yyyy- M dd", JalaliDateFormatter.FORMAT_IN_PERSIAN));
// result2 will be: ٢٨ بهمن -١٣٧٠

    }

}
