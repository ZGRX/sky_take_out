package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

/**
 * 数据统计Controller
 */
@RestController
@RequestMapping("/admin/statistics")
@Slf4j
@Api(tags = "数据统计接口")
public class StatisticsController {
    
    /**
     * 营业额统计
     */
    @GetMapping("/turnover")
    @ApiOperation("营业额统计")
    public Result<Map<String, Object>> getTurnoverStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("营业额统计：{} - {}", begin, end);
        
        // 模拟数据
        Map<String, Object> data = new HashMap<>();
        List<String> dateList = new ArrayList<>();
        List<Double> amountList = new ArrayList<>();
        
        LocalDate current = begin;
        Random random = new Random();
        while (!current.isAfter(end)) {
            dateList.add(current.toString());
            amountList.add(random.nextDouble() * 10000 + 5000);
            current = current.plusDays(1);
        }
        
        data.put("dateList", dateList);
        data.put("amountList", amountList);
        
        return Result.success(data);
    }
    
    /**
     * 订单统计
     */
    @GetMapping("/orders")
    @ApiOperation("订单统计")
    public Result<Map<String, Object>> getOrderStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("订单统计：{} - {}", begin, end);
        
        // 模拟数据
        Map<String, Object> data = new HashMap<>();
        List<String> dateList = new ArrayList<>();
        List<Integer> orderCountList = new ArrayList<>();
        
        LocalDate current = begin;
        Random random = new Random();
        while (!current.isAfter(end)) {
            dateList.add(current.toString());
            orderCountList.add(random.nextInt(50) + 10);
            current = current.plusDays(1);
        }
        
        data.put("dateList", dateList);
        data.put("orderCountList", orderCountList);
        
        return Result.success(data);
    }
    
    /**
     * 销量排行Top10
     */
    @GetMapping("/top10")
    @ApiOperation("销量排行Top10")
    public Result<Map<String, Object>> getTop10(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("销量排行Top10：{} - {}", begin, end);
        
        // 模拟数据
        Map<String, Object> data = new HashMap<>();
        data.put("nameList", Arrays.asList(
            "宫保鸡丁", "鱼香肉丝", "麻婆豆腐", "回锅肉", "糖醋里脊",
            "青椒肉丝", "酸辣土豆丝", "西红柿炒蛋", "地三鲜", "红烧肉"
        ));
        data.put("numberList", Arrays.asList(156, 142, 138, 125, 118, 102, 95, 87, 76, 65));
        
        return Result.success(data);
    }
}
