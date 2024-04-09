import React from "react";
import { useSelector } from "react-redux";
import { Navigate, Route, Routes } from "react-router-dom";
import AdminConsole from "./views/AdminConsole";
import Home from "./views/Home";
import HorizontalMenu from "./components/HorizontalMenu";
import Login from "./views/Login";
import NextTopic from "./views/NextTopic";
import Register from "./views/Register";

function App() {

    const currentUser = useSelector((state) => state.user);
    const isLoggedIn = currentUser?.token;

    return (
        <div className="App">
            {isLoggedIn && <HorizontalMenu/>}
            <Routes>
                {!isLoggedIn ? (
                    <>
                        <Route path="/" element={<Navigate to="/login"/>}/>
                        <Route path="/login" element={<Login/>}/>
                        <Route path="/register" element={<Register/>}/>
                    </>
                ) : (
                    <>
                        <Route path="/nextTopic" element={<NextTopic/>}/>
                        <Route path="/adminConsole" element={<AdminConsole/>}/>
                        <Route path="/*" element={<Home/>}/>
                    </>
                )}
            </Routes>
        </div>
    );
}

export default App;
