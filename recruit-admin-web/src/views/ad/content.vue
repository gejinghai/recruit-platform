<template>
  <div class="ad-content">
    <el-card>
      <div class="toolbar">
        <el-select v-model="query.positionId" :placeholder="$t('ad.searchPlaceholder')" clearable style="width: 200px" @change="handleSearch">
          <el-option v-for="p in positions" :key="p.id" :label="p.positionName" :value="p.id" />
        </el-select>
        <el-button type="primary" style="margin-left: 10px" @click="handleSearch">
          <el-icon><Search /></el-icon>
          {{ $t('common.search') }}
        </el-button>
        <el-button type="success" style="margin-left: 10px" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          {{ $t('ad.addAd') }}
        </el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" border stripe style="margin-top: 20px">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="positionKey" :label="$t('ad.position')" width="120" />
        <el-table-column prop="title" :label="$t('ad.title')" min-width="150" show-overflow-tooltip />
        <el-table-column :label="$t('ad.image')" width="120">
          <template #default="{ row }">
            <el-image v-if="row.imageUrl" :src="row.imageUrl" style="width: 80px; height: 40px" fit="cover" preview-teleported />
          </template>
        </el-table-column>
        <el-table-column prop="linkUrl" :label="$t('ad.linkUrl')" min-width="150" show-overflow-tooltip />
        <el-table-column prop="sortOrder" :label="$t('ad.sort')" width="80" />
        <el-table-column :label="$t('common.status')" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">
              {{ row.status === 1 ? $t('common.enable') : $t('common.disable') }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column :label="$t('common.operation')" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">
              <el-icon><Edit /></el-icon>
              {{ $t('common.edit') }}
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

    <el-dialog v-model="dialogVisible" :title="isEdit ? $t('ad.editAd') : $t('ad.addAd')" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item :label="$t('ad.position')" prop="positionId">
          <el-select v-model="form.positionId" :placeholder="$t('ad.position')" style="width: 100%">
            <el-option v-for="p in positions" :key="p.id" :label="p.positionName" :value="p.id" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('ad.title')" prop="title">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item :label="$t('ad.imageUrl')" prop="imageUrl">
          <el-input v-model="form.imageUrl" :placeholder="$t('ad.imageUrl')" />
        </el-form-item>
        <el-form-item :label="$t('ad.linkUrl')">
          <el-input v-model="form.linkUrl" />
        </el-form-item>
        <el-form-item :label="$t('ad.sort')">
          <el-input-number v-model="form.sortOrder" :min="0" />
        </el-form-item>
        <el-form-item :label="$t('common.status')">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">{{ $t('common.enable') }}</el-radio>
            <el-radio :label="0">{{ $t('common.disable') }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
        <el-button type="primary" @click="handleSubmit">{{ $t('common.save') }}</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getAdPositions, getAdContentList, saveAdContent, deleteAdContent } from '../../api/ad'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useI18n } from 'vue-i18n'
import { Search, Plus, Edit, Delete } from '@element-plus/icons-vue'

const { t } = useI18n()
const positions = ref([])
const tableData = ref([])
const total = ref(0)
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref()
const query = reactive({
  positionId: null,
  page: 1,
  pageSize: 10
})

const form = ref({
  id: null,
  positionId: null,
  title: '',
  imageUrl: '',
  linkUrl: '',
  sortOrder: 0,
  status: 1
})

const rules = {
  positionId: [{ required: true, message: '', trigger: 'change' }],
  title: [{ required: true, message: '', trigger: 'blur' }],
  imageUrl: [{ required: true, message: '', trigger: 'blur' }]
}

const loadPositions = async () => {
  try {
    const res = await getAdPositions()
    positions.value = res.data || []
  } catch (e) {
    console.error(e)
  }
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getAdContentList()
    let allData = res.data || []
    if (query.positionId) {
      allData = allData.filter(item => item.positionId === query.positionId)
    }
    total.value = allData.length
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

const handleAdd = () => {
  form.value = { id: null, positionId: null, title: '', imageUrl: '', linkUrl: '', sortOrder: 0, status: 1 }
  isEdit.value = false
  dialogVisible.value = true
}

const handleEdit = (row) => {
  form.value = { ...row }
  isEdit.value = true
  dialogVisible.value = true
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(t('ad.deleteConfirm'), t('common.warning'), { type: 'warning' })
    await deleteAdContent(row.id)
    ElMessage.success(t('ad.deleteSuccess'))
    loadData()
  } catch (e) {
    if (e !== 'cancel') {
      console.error(e)
    }
  }
}

const handleSubmit = async () => {
  await formRef.value.validate()
  await saveAdContent(form.value)
  ElMessage.success(t('ad.saveSuccess'))
  dialogVisible.value = false
  loadData()
}

onMounted(() => {
  loadPositions()
  loadData()
})
</script>

<style scoped>
.ad-content {
  padding: 20px;
}
.toolbar {
  display: flex;
  align-items: center;
}
.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
