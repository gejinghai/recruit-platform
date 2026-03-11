<template>
  <view class="container">
    <view class="about-card">
      <view class="logo">招聘平台</view>
      <view class="version">Version 1.0.0</view>
      <view class="desc">
        本平台为临时工招聘专用，用于展示招聘信息，帮助求职者快速找到合适的工作。
      </view>
    </view>

    <view class="contact-card">
      <view class="contact-title">联系我们</view>
      <view class="contact-item">
        <text class="label">电话：</text>
        <text>138-0000-0000</text>
      </view>
      <view class="contact-item">
        <text class="label">邮箱：</text>
        <text>contact@recruit.com</text>
      </view>
    </view>
  </view>
</template>

<script setup>
</script>

<style scoped>
.container {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding: 30rpx;
}
.about-card {
  background-color: #fff;
  border-radius: 16rpx;
  padding: 50rpx 30rpx;
  text-align: center;
  margin-bottom: 30rpx;
}
.logo {
  font-size: 40rpx;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 20rpx;
}
.version {
  color: #999;
  font-size: 24rpx;
  margin-bottom: 30rpx;
}
.desc {
  color: #666;
  font-size: 28rpx;
  line-height: 1.8;
}
.contact-card {
  background-color: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
}
.contact-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
}
.contact-item {
  display: flex;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f5f5f5;
}
.label {
  color: #999;
  width: 120rpx;
}
</style>
