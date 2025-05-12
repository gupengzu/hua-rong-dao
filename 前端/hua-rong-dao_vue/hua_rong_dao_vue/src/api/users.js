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

//获奖数加一
export const addAward = (username) => {
    return request.post('/users/award', { "username":username });
}

export const storeLayout = (data) => {
    return request.post('/users/storeLayout', data);
}

export const getLayout = (username) => {
    return request.post('/users/getLayout', { "username": username });
}

//当用户进入Game界面后，发送请求，向“当前正在游玩的用户列表”中添加一条信息
export const addPlayingUser = (data) => {
    return request.post('/users/addPlayingUser', data);
}
//当用户的layout发生变化时，发送请求，向“当前正在游玩的用户列表”中修改layout信息
export const changePlayingUser = (data) => {
    return request.post('/users/changePlayingUser', data);
}

//当用户退出Game界面后，发送请求，向“当前正在游玩的用户列表”中删除一条信息
export const deletePlayingUser = (data) => {
    return request.post('/users/deletePlayingUser', data);
}

//返回当前正在观战界面游玩的用户列表
export const getPlayingUsers = () => {
    return request.get('/users/getPlayingUsers');
}
