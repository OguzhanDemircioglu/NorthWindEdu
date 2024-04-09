import {BASE_URL} from "../store/Enums";
import axios from "axios";
import Store from "../store";

const currentUser = Store.getState().user;

class AuthService {

    register(email, password, username, callback) {
        fetch(BASE_URL + '/auth/register', {
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
    }

    login(password, username, callback) {
        fetch(BASE_URL + '/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                email: null, password: password, username: username
            })
        })
            .then(response => {
                if (!response.ok) {
                    alert("İşlem şuan gerçekleştirilemiyor")
                    return;
                }
                return response.json();
            })
            .then(data => {
                if (data && data.token) {
                    callback(data.token,data.role);
                } else {
                    console.error("Giriş yapılamadı: Token alınamadı.");
                }
            })
            .catch(error => {
                console.error('Error fetching data:', error);
            });
    }

    findAllUsers(callback) {
        return axios.get(
            BASE_URL + '/auth/findAll',
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

    deleteUserById(id) {
        return axios.delete(
            BASE_URL + `/auth/deleteUserById/${id}`,
            {
                headers: {
                    "Content-Type": "application/json",
                    authorization: "Bearer " + currentUser?.token,
                }
            }
        )
    }

    insertUser(formData) {
        return axios.post(
            BASE_URL + '/auth/insertUser',
            formData,
            {
                headers: {
                    "Content-Type": "application/json",
                    authorization: "Bearer " + currentUser?.token,
                }
            }
        )
    }

    updateUser(formUpdateData) {
        return axios.put(
            BASE_URL + '/auth/updateUser',
            formUpdateData,
            {
                headers: {
                    "Content-Type": "application/json",
                    authorization: "Bearer " + currentUser?.token,
                }
            }
        )
    }
}

export default new AuthService();