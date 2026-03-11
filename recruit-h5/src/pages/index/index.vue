<template>
  <view class="container">
    <!-- 搜索栏 -->
    <view class="search-bar">
      <input type="text" v-model="keyword" placeholder="搜索职位/企业" @confirm="loadData" />
    </view>

    <!-- 轮播图 -->
    <swiper class="banner" indicator-dots autoplay circular v-if="banners.length > 0">
      <swiper-item v-for="(item, index) in banners" :key="index">
        <image :src="item.imageUrl" mode="aspectFill" class="banner-img" />
      </swiper-item>
    </swiper>

    <!-- 职位列表 -->
    <view class="job-list">
      <view class="job-item" v-for="item in list" :key="item.id" @click="goDetail(item.id)">
        <view class="job-header">
          <text class="job-title">{{ item.title }}</text>
          <text class="job-salary" v-if="item.isTop === 1">置顶</text>
        </view>
        <view class="job-company">{{ item.companyName }}</view>
        <view class="job-info">
          <text class="job-tag">{{ item.workType }}</text>
          <text class="job-tag">{{ item.salary }}</text>
        </view>
        <view class="job-welfare" v-if="item.welfare">
          <text class="welfare-item" v-for="(w, i) in parseWelfare(item.welfare)" :key="i">{{ w }}</text>
        </view>
      </view>

      <view class="empty" v-if="list.length === 0">
        <text>暂无招聘信息</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getJobList, getBanner } from '../../api/index'

const list = ref([])
const banners = ref([])
const keyword = ref('')

const loadData = async () => {
  try {
    const res = await getJobList({ keyword: keyword.value, status: 1 })
    list.value = res.data || []
  } catch (e) {
    console.error(e)
  }
}

const loadBanner = async () => {
  try {
    const res = await getBanner()
    banners.value = res.data || []
  } catch (e) {
    console.error(e)
  }
}

const parseWelfare = (welfare) => {
  try {
    return JSON.parse(welfare)
  } catch (e) {
    return []
  }
}

const goDetail = (id) => {
  uni.navigateTo({
    url: `/pages/job/detail?id=${id}`
  })
}

onMounted(() => {
  loadData()
  loadBanner()
})
</script>

<style scoped>
.container {
  min-height: 100vh;
  background-color: #f5f5f5;
}
.search-bar {
  background-color: #fff;
  padding: 20rpx;
}
.search-bar input {
  background-color: #f5f5f5;
  border-radius: 40rpx;
  padding: 16rpx 30rpx;
  font-size: 28rpx;
}
.banner {
  height: 300rpx;
}
.banner-img {
  width: 100%;
  height: 100%;
}
.job-list {
  padding: 20rpx;
}
.job-item {
  background-color: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
}
.job-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16rpx;
}
.job-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}
.job-salary {
  color: #ff6b6b;
  font-size: 28rpx;
  font-weight: bold;
}
.job-company {
  color: #666;
  font-size: 26rpx;
  margin-bottom: 16rpx;
}
.job-info {
  display: flex;
  gap: 16rpx;
  margin-bottom: 16rpx;
}
.job-tag {
  background-color: #e8f4ff;
  color: #409eff;
  padding: 8rpx 16rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
}
.job-welfare {
  display: flex;
  flex-wrap: wrap;
  gap: 12rpx;
}
.welfare-item {
  background-color: #fff7e6;
  color: #ff9800;
  padding: 6rpx 12rpx;
  border-radius: 6rpx;
  font-size: 22rpx;
}
.empty {
  text-align: center;
  padding: 100rpx;
  color: #999;
}
</style>
