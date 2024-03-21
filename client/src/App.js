import Login from "./components/Login";
import {Navigate, Route, Routes} from "react-router-dom";
import Register from "./components/Register";
import HorizontalMenu from "./components/HorizontalMenu";
import React from "react";
import Home from "./components/Home";
import {useSelector} from "react-redux";
import NextTopic from "./components/NextTopic";

function App() {

    const currentUser = useSelector((state) => state.user);

    const isLoggedIn = currentUser?.token;

    return (
        <div className="App">
            <HorizontalMenu/>
            <Routes>
                {!isLoggedIn ? (
                    <>
                        <Route path="/" element={<Navigate to="/login"/>}/>
                        <Route path="/nextTopic" element={<Navigate to="/login"/>}/>
                        <Route path="/login" element={<Login/>}/>
                        <Route path="/register" element={<Register/>}/>
                    </>
                ) : (
                    <>
                        <Route path="/nextTopic" element={<NextTopic/>}/>
                        <Route path="/" element={<Home/>}/>
                    </>
                )}
            </Routes>
        </div>
    );
}

export default App;
