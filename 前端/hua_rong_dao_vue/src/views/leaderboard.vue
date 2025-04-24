
<script setup>
import { ref,onMounted } from 'vue'
import { queryAll } from '@/api/users'


onMounted(() => {
    show()
})
const show=async () => {
    const result = await queryAll()
    if(result.code){}
    tableData.value = result.data;
}


const tableData = ref([
    
])

</script>

<template>
    <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="id" label="id" />
        <el-table-column prop="username" label="用户名" width="180" />
        <el-table-column prop="award_count" label="获奖次数" width="180" />
        <el-table-column prop="image" label="头像">
            <template #default="{ row }">
                <img :src="row.image" alt="头像" style="width: 30px; height: 30px; border-radius: 50%;" />
            </template>
        </el-table-column>
    </el-table>
</template>


<style scoped>

</style>
