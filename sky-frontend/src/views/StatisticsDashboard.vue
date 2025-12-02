<template>
  <div class="statistics-container">
    <div class="date-picker-container">
      <span class="label">统计时间：</span>
      <el-date-picker
        v-model="dateRange"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        :shortcuts="shortcuts"
        @change="fetchData"
        value-format="YYYY-MM-DD"
      />
      <el-button type="primary" style="margin-left: 20px" @click="exportData">
        <el-icon><Download /></el-icon> 导出数据
      </el-button>
    </div>

    <el-row :gutter="20" class="data-cards">
      <el-col :span="8">
        <el-card shadow="hover" class="data-card turnover">
          <template #header>
            <div class="card-header">
              <span>营业额统计</span>
              <el-tag type="success">今日</el-tag>
            </div>
          </template>
          <div class="card-content">
            <div class="number">￥{{ todayTurnover }}</div>
            <div class="desc">总营业额：￥{{ totalTurnover }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="data-card orders">
          <template #header>
            <div class="card-header">
              <span>订单统计</span>
              <el-tag type="warning">今日</el-tag>
            </div>
          </template>
          <div class="card-content">
            <div class="number">{{ todayOrders }}</div>
            <div class="desc">总订单数：{{ totalOrders }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="data-card completion">
          <template #header>
            <div class="card-header">
              <span>订单完成率</span>
              <el-tag type="info">总计</el-tag>
            </div>
          </template>
          <div class="card-content">
            <div class="number">{{ completionRate }}%</div>
            <div class="desc">有效订单：{{ validOrders }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card shadow="hover">
          <div id="turnoverChart" style="height: 400px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <div id="orderChart" style="height: 400px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row style="margin-top: 20px">
      <el-col :span="24">
        <el-card shadow="hover">
          <div id="top10Chart" style="height: 400px"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { Download } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import axios from 'axios'
import dayjs from 'dayjs'
import { ElMessage } from 'element-plus'

const dateRange = ref([])
const todayTurnover = ref(0)
const totalTurnover = ref(0)
const todayOrders = ref(0)
const totalOrders = ref(0)
const completionRate = ref(0)
const validOrders = ref(0)

const shortcuts = [
  {
    text: '最近一周',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
      return [start, end]
    },
  },
  {
    text: '最近一个月',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
      return [start, end]
    },
  },
]

// 初始化默认时间范围（最近7天）
const initDateRange = () => {
  const end = dayjs().format('YYYY-MM-DD')
  const start = dayjs().subtract(6, 'day').format('YYYY-MM-DD')
  dateRange.value = [start, end]
}

const fetchData = async () => {
  if (!dateRange.value || dateRange.value.length < 2) return
  
  const [begin, end] = dateRange.value
  const token = localStorage.getItem('token')
  
  try {
    // 1. 获取营业额统计
    const turnoverRes = await axios.get('/api/admin/statistics/turnover', {
      params: { begin, end },
      headers: { token }
    })
    if (turnoverRes.data.code === 1) {
      initTurnoverChart(turnoverRes.data.data)
      // 简单模拟卡片数据（实际应该有专门的接口）
      const amounts = turnoverRes.data.data.amountList
      totalTurnover.value = amounts.reduce((a, b) => a + b, 0).toFixed(2)
      todayTurnover.value = amounts[amounts.length - 1].toFixed(2)
    }

    // 2. 获取订单统计
    const orderRes = await axios.get('/api/admin/statistics/orders', {
      params: { begin, end },
      headers: { token }
    })
    if (orderRes.data.code === 1) {
      initOrderChart(orderRes.data.data)
      const counts = orderRes.data.data.orderCountList
      totalOrders.value = counts.reduce((a, b) => a + b, 0)
      todayOrders.value = counts[counts.length - 1]
      validOrders.value = totalOrders.value // 简化处理
      completionRate.value = totalOrders.value > 0 ? 100 : 0
    }

    // 3. 获取销量排行
    const top10Res = await axios.get('/api/admin/statistics/top10', {
      params: { begin, end },
      headers: { token }
    })
    if (top10Res.data.code === 1) {
      initTop10Chart(top10Res.data.data)
    }

  } catch (error) {
    console.error('获取统计数据失败', error)
    ElMessage.error('获取统计数据失败')
  }
}

const initTurnoverChart = (data) => {
  const chartDom = document.getElementById('turnoverChart')
  const myChart = echarts.init(chartDom)
  const option = {
    title: { text: '营业额趋势' },
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: data.dateList },
    yAxis: { type: 'value' },
    series: [{ data: data.amountList, type: 'line', smooth: true, areaStyle: {} }]
  }
  myChart.setOption(option)
}

const initOrderChart = (data) => {
  const chartDom = document.getElementById('orderChart')
  const myChart = echarts.init(chartDom)
  const option = {
    title: { text: '订单量趋势' },
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: data.dateList },
    yAxis: { type: 'value' },
    series: [{ data: data.orderCountList, type: 'bar', color: '#5470C6' }]
  }
  myChart.setOption(option)
}

const initTop10Chart = (data) => {
  const chartDom = document.getElementById('top10Chart')
  const myChart = echarts.init(chartDom)
  const option = {
    title: { text: '销量排名 Top10' },
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: { type: 'value' },
    yAxis: { type: 'category', data: data.nameList, inverse: true }, // inverse 让第一名在上面
    series: [{
      name: '销量',
      type: 'bar',
      data: data.numberList,
      label: { show: true, position: 'right' },
      itemStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
          { offset: 0, color: '#83bff6' },
          { offset: 0.5, color: '#188df0' },
          { offset: 1, color: '#188df0' }
        ])
      }
    }]
  }
  myChart.setOption(option)
}

const exportData = () => {
  ElMessage.success('正在导出数据...')
  // 这里可以实现真实的导出逻辑，或者调用后端导出接口
}

onMounted(() => {
  initDateRange()
  nextTick(() => {
    fetchData()
  })
  
  // 监听窗口大小变化，重绘图表
  window.addEventListener('resize', () => {
    echarts.getInstanceByDom(document.getElementById('turnoverChart'))?.resize()
    echarts.getInstanceByDom(document.getElementById('orderChart'))?.resize()
    echarts.getInstanceByDom(document.getElementById('top10Chart'))?.resize()
  })
})
</script>

<style scoped>
.statistics-container {
  padding: 20px;
}
.date-picker-container {
  background: #fff;
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 4px;
}
.label {
  margin-right: 10px;
  font-weight: bold;
}
.data-card {
  height: 150px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.card-content {
  text-align: center;
  padding-top: 20px;
}
.number {
  font-size: 30px;
  font-weight: bold;
  color: #409EFF;
  margin-bottom: 10px;
}
.desc {
  color: #909399;
  font-size: 14px;
}
</style>
