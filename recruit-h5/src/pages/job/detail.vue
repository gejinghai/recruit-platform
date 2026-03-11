<template>
  <view class="container" v-if="job.id">
    <view class="header">
      <view class="title">{{ job.title }}</view>
      <view class="salary">{{ job.salary }}</view>
      <view class="tags">
        <text class="tag">{{ job.workType }}</text>
        <text class="tag">{{ job.workTime }}</text>
      </view>
    </view>

    <view class="section">
      <view class="section-title">企业简介</view>
      <view class="section-content">{{ job.description }}</view>
    </view>

    <view class="section">
      <view class="section-title">招聘要求</view>
      <view class="section-content">{{ job.requirements }}</view>
    </view>

    <view class="section" v-if="job.collectionTime || job.address">
      <view class="section-title">面试信息</view>
      <view class="info-item" v-if="job.collectionTime">
        <text class="label">集合时间：</text>
        <text>{{ job.collectionTime }}</text>
      </view>
      <view class="info-item" v-if="job.address">
        <text class="label">面试地址：</text>
        <text>{{ job.address }}</text>
      </view>
    </view>

    <view class="footer">
      <button class="btn-contact" @click="handleContact">联系报名</button>
    </view>

    <!-- 联系信息弹窗 -->
    <view class="contact-modal" v-if="showContact" @click="showContact = false">
      <view class="contact-content" @click.stop>
        <view class="contact-title">联系方式</view>
        <view class="contact-body">
          <text v-if="job.contactType === 'text'" class="contact-text">{{ job.contactValue }}</text>
          <image v-else :src="job.contactValue" mode="aspectFit" class="contact-image" @click="previewImage" />
        </view>
        <view class="contact-tip" v-if="job.contactType === 'text'">请复制上方联系方式进行联系</view>
        <view class="contact-tip" v-else>长按图片保存或扫码添加</view>
        <button class="btn-close" @click="showContact = false">关闭</button>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getJobDetail } from '../../api/index'

const job = ref({})
const id = ref('')
const showContact = ref(false)

const loadData = async () => {
  try {
    const res = await getJobDetail(id.value)
    job.value = res.data || {}
  } catch (e) {
    console.error(e)
  }
}

const handleContact = () => {
  if (!job.value.contactValue) {
    uni.showToast({
      title: '暂无联系方式',
      icon: 'none'
    })
    return
  }
  showContact.value = true
}

const previewImage = () => {
  if (job.value.contactType === 'image' && job.value.contactValue) {
    uni.previewImage({
      urls: [job.value.contactValue]
    })
  }
}

onMounted(() => {
  const pages = getCurrentPages()
  const currentPage = pages[pages.length - 1]
  id.value = currentPage.options.id
  loadData()
})
</script>

<style scoped>
.container {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 120rpx;
}
.header {
  background-color: #fff;
  padding: 30rpx;
  margin-bottom: 20rpx;
}
.title {
  font-size: 36rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 16rpx;
}
.salary {
  font-size: 40rpx;
  color: #ff6b6b;
  font-weight: bold;
  margin-bottom: 16rpx;
}
.tags {
  display: flex;
  gap: 16rpx;
}
.tag {
  background-color: #e8f4ff;
  color: #409eff;
  padding: 8rpx 16rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
}
.section {
  background-color: #fff;
  padding: 30rpx;
  margin-bottom: 20rpx;
}
.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
  padding-left: 16rpx;
  border-left: 6rpx solid #409eff;
}
.section-content {
  color: #666;
  font-size: 28rpx;
  line-height: 1.8;
}
.info-item {
  display: flex;
  margin-bottom: 16rpx;
}
.label {
  color: #999;
  width: 180rpx;
}
.footer {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: #fff;
  padding: 20rpx 30rpx;
  box-shadow: 0 -2rpx 10rpx rgba(0,0,0,0.05);
}
.btn-contact {
  background-color: #409eff;
  color: #fff;
  border-radius: 50rpx;
  font-size: 32rpx;
}

/* 联系弹窗样式 */
.contact-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
}
.contact-content {
  background-color: #fff;
  border-radius: 20rpx;
  padding: 40rpx;
  width: 600rpx;
  text-align: center;
}
.contact-title {
  font-size: 32rpx;
  font-weight: bold;
  margin-bottom: 30rpx;
}
.contact-body {
  min-height: 150rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20rpx;
}
.contact-text {
  font-size: 36rpx;
  color: #409eff;
  font-weight: bold;
  word-break: break-all;
}
.contact-image {
  width: 300rpx;
  height: 300rpx;
}
.contact-tip {
  font-size: 24rpx;
  color: #999;
  margin-bottom: 30rpx;
}
.btn-close {
  background-color: #f5f5f5;
  color: #666;
  border-radius: 50rpx;
  font-size: 28rpx;
}
</style>
