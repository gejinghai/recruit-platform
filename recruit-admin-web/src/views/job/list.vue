<template>
  <div class="job-list">
    <el-card>
      <div class="toolbar">
        <el-input
          v-model="query.keyword"
          :placeholder="$t('job.searchPlaceholder')"
          style="width: 200px"
          clearable
          @clear="handleSearch"
        />
        <el-select v-model="query.status" :placeholder="$t('common.status')" clearable style="width: 120px; margin-left: 10px" @change="handleSearch">
          <el-option :label="$t('common.all')" :value="null" />
          <el-option :label="$t('common.online')" :value="1" />
          <el-option :label="$t('common.offline')" :value="0" />
        </el-select>
        <el-button type="primary" style="margin-left: 10px" @click="handleSearch">
          <el-icon><Search /></el-icon>
          {{ $t('common.search') }}
        </el-button>
        <el-button type="success" style="margin-left: 10px" @click="$router.push('/job/add')">
          <el-icon><Plus /></el-icon>
          {{ $t('job.addJob') }}
        </el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" border stripe style="margin-top: 20px">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="title" :label="$t('job.title')" min-width="180" show-overflow-tooltip />
        <el-table-column prop="companyName" :label="$t('job.companyName')" min-width="150" show-overflow-tooltip />
        <el-table-column prop="salary" :label="$t('job.salary')" width="100" />
        <el-table-column prop="workType" :label="$t('job.workType')" width="80" />
        <el-table-column prop="viewCount" :label="$t('job.viewCount')" width="80" />
        <el-table-column :label="$t('common.status')" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">
              {{ row.status === 1 ? $t('common.online') : $t('common.offline') }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column :label="$t('job.top')" width="80">
          <template #default="{ row }">
            <el-tag :type="row.isTop === 1 ? 'warning' : 'info'" size="small">
              {{ row.isTop === 1 ? $t('common.yes') : $t('common.no') }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column :label="$t('job.contactType')" width="100">
          <template #default="{ row }">
            <span v-if="row.contactType === 'text'">{{ $t('job.text') }}</span>
            <span v-else-if="row.contactType === 'image'">{{ $t('job.image') }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('common.operation')" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="$router.push(`/job/edit/${row.id}`)">
              <el-icon><Edit /></el-icon>
              {{ $t('common.edit') }}
            </el-button>
            <el-button size="small" @click="handleTop(row)">
              {{ row.isTop === 1 ? $t('job.untop') : $t('job.top') }}
            </el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">
              <el-icon><Delete /></el-icon>
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="query.page"
          v-model:page-size="query.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getJobList, deleteJob, topJob } from '../../api/job'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useI18n } from 'vue-i18n'
import { Search, Plus, Edit, Delete } from '@element-plus/icons-vue'

const { t } = useI18n()
const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const query = reactive({
  keyword: '',
  status: null,
  page: 1,
  pageSize: 10
})

const loadData = async () => {
  loading.value = true
  try {
    const res = await getJobList({ keyword: query.keyword, status: query.status })
    const allData = res.data || []
    total.value = allData.length
    // 前端分页
    const start = (query.page - 1) * query.pageSize
    const end = start + query.pageSize
    tableData.value = allData.slice(start, end)
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  query.page = 1
  loadData()
}

const handleSizeChange = (val) => {
  query.pageSize = val
  loadData()
}

const handleCurrentChange = (val) => {
  query.page = val
  loadData()
}

const handleTop = async (row) => {
  try {
    await topJob(row.id, row.isTop === 1 ? 0 : 1)
    ElMessage.success(t('job.topSuccess'))
    loadData()
  } catch (e) {
    console.error(e)
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(t('job.deleteConfirm'), t('common.warning'), { type: 'warning' })
    await deleteJob(row.id)
    ElMessage.success(t('job.deleteSuccess'))
    loadData()
  } catch (e) {
    if (e !== 'cancel') {
      console.error(e)
    }
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.job-list {
  padding: 20px;
}
.toolbar {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
}
.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
