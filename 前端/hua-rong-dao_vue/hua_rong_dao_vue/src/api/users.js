import request from '@/utils/request'

//查询所有
export const queryAll = () => {
    return request.get('/users/list');
}
    
//根据id查询
export const queryById = (id) => {
        return request.get('/users/' + id);
}
     
//根据username修改头像
export const change = (data) => {
    return request.post('/users/image', data);
};