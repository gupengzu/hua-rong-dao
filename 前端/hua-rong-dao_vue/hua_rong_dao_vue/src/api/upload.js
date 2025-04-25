import request from '@/utils/request'

//从用户端提交图片到阿里云
export const uploadImage = (file) => {
    const formData = new FormData();
    formData.append('file', file); //将文件添加到formData对象中，键名为'file'，键值为file对象
    return request.post('/upload', formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }//这个headers的作用是告诉服务器，当前请求的内容类型是 multipart/form-data，用于上传文件或表单数据，如果没有headers可能会失败
    })
}