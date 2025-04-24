import request from '@/utils/request'

//展示排名
export const queryAll = () => {
    return request.get('/users/list');
    }