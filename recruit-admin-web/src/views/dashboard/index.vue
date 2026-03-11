<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background: #409EFF">
              <el-icon><Briefcase /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.totalJobs }}</div>
              <div class="stat-label">{{ $t('dashboard.totalJobs') }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background: #67C23A">
              <el-icon><CircleCheck /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.onlineJobs }}</div>
              <div class="stat-label">{{ $t('dashboard.onlineJobs') }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background: #E6A23C">
              <el-icon><View /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.totalViews }}</div>
              <div class="stat-label">{{ $t('dashboard.totalViews') }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background: #F56C6C">
              <el-icon><Picture /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.totalAds }}</div>
              <div class="stat-label">{{ $t('dashboard.totalAds') }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getJobList } from '../../api/job'
import { getAdContentList } from '../../api/ad'

const stats = ref({
  totalJobs: 0,
  onlineJobs: 0,
  totalViews: 0,
  totalAds: 0
})

const loadStats = async () => {
  try {
    const [jobRes, adRes] = await Promise.all([
      getJobList(),
      getAdContentList()
    ])
    const jobs = jobRes.data || []
    stats.value.totalJobs = jobs.length
    stats.value.onlineJobs = jobs.filter(j => j.status === 1).length
    stats.value.totalViews = jobs.reduce((sum, j) => sum + (j.viewCount || 0), 0)
    stats.value.totalAds = (adRes.data || []).length
  } catch (e) {
    console.error(e)
  }
}

onMounted(() => {
  loadStats()
})
</script>

<style scoped>
.dashboard {
  padding: 20px;
}
.stat-card {
  display: flex;
  align-items: center;
}
.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: #fff;
  margin-right: 15px;
  flex-shrink: 0;
}
.stat-value {
  font-size: 28px;
  font-weight: bold;
}
.stat-label {
  font-size: 14px;
  color: #999;
}
</style>
