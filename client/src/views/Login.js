import React, {useEffect, useRef, useState} from 'react';
import "../App.css";
import {useNavigate} from "react-router-dom";
import {useDispatch} from "react-redux";
import {setCurrentUser} from "../store/actions";
import AuthService from "../services/AuthService";

const Login = () => {
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const usernameRef = useRef();
    const passwordRef = useRef();
    const btnRef = useRef();

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const submitForm = () => {
        AuthService.login(password, username, (token, role) => {
            const returnVal = {username: username, token: token, role: role};
            dispatch(setCurrentUser(returnVal));
            if (token) {
                navigate('/');
            }
        });
    };

    useEffect(() => {
        // Her ikisi de doluysa buton konumunu sıfırla
        if (username.trim() && password.trim()) {
            const btn = btnRef.current;
            btn.style.transform = 'translate(0, 0)';
        }
    }, [username, password]);

    const shiftButton = () => {
        const btn = btnRef.current;

        const randomX = (Math.random() - 0.5) * 500;
        const randomY = (Math.random() - 0.5) * 200;

        btn.style.transform = `translate(${randomX}px, ${randomY}px)`;
    };

    const handleMouseEnter = () => {
        if (!username.trim() || !password.trim()) {
            shiftButton();
        }
    };

    const handleLoginClick = () => {
        if (username.trim() && password.trim()) {
            submitForm();
        }
    };

    return (
        <div style={{marginTop: "30px"}} className="row justify-content-center pt-5">
            <div className="col-sm-6">
                <div className="card p-4">
                    <h1 className="text-center mb-3">Login</h1>

                    {/* Form etiketi: autocomplete off + fake fields */}
                    <form autoComplete="off">
                        {/* Tarayıcıyı kandırmak için fake inputlar */}
                        <input
                            type="text"
                            name="fake-user"
                            autoComplete="username"
                            style={{display: 'none'}}
                        />
                        <input
                            type="password"
                            name="fake-pass"
                            autoComplete="new-password"
                            style={{display: 'none'}}
                        />

                        {/* Gerçek kullanıcı adı inputu */}
                        <div className="form-group">
                            <label>Username:</label>
                            <input
                                type="text"
                                className="form-control"
                                placeholder="Enter username"
                                ref={usernameRef}
                                id="username"
                                name="real-username"
                                autoComplete="off"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                            />
                        </div>

                        {/* Gerçek şifre inputu */}
                        <div className="form-group mt-3">
                            <label>Password:</label>
                            <input
                                type="password"
                                className="form-control"
                                placeholder="Enter password"
                                ref={passwordRef}
                                id="pwd"
                                name="real-password"
                                autoComplete="new-password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                            />
                        </div>

                        <button
                            type="button"
                            ref={btnRef}
                            onMouseEnter={handleMouseEnter}
                            onClick={handleLoginClick}
                            className="btn btn-primary mt-4 w-100"
                        >
                            Login
                        </button>
                    </form>
                    <br/>
                    <label>Need an Account?</label>
                    <button
                        type="button"
                        onClick={() => navigate("/register")}
                        className="btn btn-primary"
                    >
                        Register
                    </button>
                </div>
            </div>
        </div>
    );
};

export default Login;