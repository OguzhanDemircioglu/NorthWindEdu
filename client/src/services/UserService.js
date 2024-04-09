import {BASE_URL} from "../store/Enums";
import Store from "../store";
import axios from "axios";

const currentUser = Store.getState().user;

class UserService{

/*    insertUser(email, password, username, callback) {
        fetch(BASE_URL + '/user/insertUser', {
            method: 'POST', headers: {
                'Content-Type': 'application/json'
            }, body: JSON.stringify({
                email: email, password: password, username: username
            })
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('İşlem şuan gerçekleştirilemiyor');
                }

                callback(response.json());
            })
            .catch(error => {
                console.error('Error fetching data:', error);
            });
    }*/

/*    findAllUsers(callback) {
        return axios.post(
            BASE_URL + '/user/findAll',
            {},
            {
                headers: {
                    "Content-Type": "application/json",
                    authorization: "Bearer " + currentUser?.token,
                }
            }
        ).then(res => {
            callback(res.data)
        }).catch(err => console.log(err));
    }*/

    findAllUsers(callback) {
        return axios.post(
            BASE_URL + '/user/findAll',
            {},
            {
                headers: {
                    "Content-Type": "application/json",
                    authorization: "Bearer " + currentUser?.token,
                }
            }
        ).then(res => {
            callback(res.data)
        }).catch(err => console.log(err));
    }
}

export default new UserService();