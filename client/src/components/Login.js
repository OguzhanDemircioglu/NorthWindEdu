import React, {useRef} from 'react';
import "../App.css";
import {useNavigate} from "react-router-dom";
import {useDispatch} from "react-redux";
import {setCurrentUser} from "../store/action/user";
import AuthService from "../services/AuthService";

const Login = () => {
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const usernameRef = useRef();
    const paswordRef = useRef();

    const submitForm = () => {
        AuthService.login(paswordRef.current.value, usernameRef.current.value, (token) => {

            const returnVal = {username: usernameRef.current.value, token: token};
            dispatch(setCurrentUser(returnVal));
            if (token) {
                new Promise(resolve => {
                    resolve();
                }).then(() => {
                    navigate('/');
                    window.location.reload();
                });
            }
        });
    }

    return (
        <div style={{marginTop: "30px"}} className="row justify-content-center pt-5">
            <div className="col-sm-6">
                <div className="card p-4">
                    <h1 className="text-center mb-3">Login </h1>
                    <div className="form-group">
                        <label>Username:</label>
                        <input type="username" className="form-control" placeholder="Enter username"
                               ref={usernameRef}
                               id="username"/>
                    </div>
                    <div className="form-group mt-3">
                        <label>Password:</label>
                        <input type="password" className="form-control" placeholder="Enter password"
                               ref={paswordRef}
                               id="pwd"/>
                    </div>
                    <button type="button" onClick={submitForm} className="btn btn-primary mt-4">Login</button>
                    <br/>
                    <label> Need an Account?</label>
                    <button type="button" onClick={() => navigate("/register")}
                            className="btn btn-primary">Register
                    </button>
                </div>
            </div>

        </div>
    );
};

export default Login;