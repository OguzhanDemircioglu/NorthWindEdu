import {BASE_URL} from "../store/Enums";

class AuthService {

    register(email, password, username, callback) {
        fetch(BASE_URL + '/auth/signUp', {
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
        fetch(BASE_URL + '/auth/signIn', {
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
                    callback(data.token);
                } else {
                    console.error("Giriş yapılamadı: Token alınamadı.");
                }
            })
            .catch(error => {
                console.error('Error fetching data:', error);
            });
    }
}

export default new AuthService();