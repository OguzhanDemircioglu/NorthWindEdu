import React, { useEffect } from "react";
import { useSelector } from "react-redux";
import { Navigate, Route, Routes } from "react-router-dom";
import AdminConsole from "./views/AdminConsole";
import Home from "./views/Home";
import HorizontalMenu from "./components/HorizontalMenu";
import VerticalMenu from "./components/VerticalMenu";
import Login from "./views/Login";
import Register from "./views/Register";
import CategoryList from "./views/CategoryList";
import CustomerList from "./views/CustomerList";
import EmployeeList from "./views/EmployeeList";
import OrderList from "./views/OrderList";
import OrderDetailList from "./views/OrderDetailList";
import CustomerDemographicsList from "./views/CustomerDemographicsList";
import CustomerDemoList from "./views/CustomerDemoList";

function App() {
    const currentUser = useSelector((state) => state.user);
    const isLoggedIn = currentUser?.token;

    useEffect(() => {
        const loadEthereum = async () => {
            if (typeof window.ethereum !== "undefined") {
                try {
                    await window.ethereum.request({ method: "eth_requestAccounts" });
                    console.log("MetaMask is connected");
                } catch (error) {
                    console.error("User denied account access", error);
                }
            } else {
                console.error("Ethereum provider (MetaMask) not found");
            }
        };

        window.addEventListener("load", loadEthereum);
        return () => window.removeEventListener("load", loadEthereum);
    }, []);

    return (
        <div className="App">
            {isLoggedIn && <HorizontalMenu />}
            {isLoggedIn && <VerticalMenu />}

            <div style={{ marginLeft: isLoggedIn ? "220px" : 0, marginTop: isLoggedIn ? "56px" : 0 }}>
                <Routes>
                    {!isLoggedIn ? (
                        <>
                            <Route path="/" element={<Navigate to="/login" />} />
                            <Route path="/login" element={<Login />} />
                            <Route path="/register" element={<Register />} />
                        </>
                    ) : (
                        <>
                            <Route path="/adminConsole" element={<AdminConsole />} />
                            <Route path="/*" element={<Home />} />

                            <Route path="/categories" element={<CategoryList />} />
                            <Route path="/customers" element={<CustomerList />} />
                            <Route path="/employees" element={<EmployeeList />} />
                            <Route path="/orders" element={<OrderList />} />
                            <Route path="/order-details" element={<OrderDetailList />} />
                            <Route path="/customer-demographics" element={<CustomerDemographicsList />} />
                            <Route path="/customer-demos" element={<CustomerDemoList />} />
                        </>
                    )}
                </Routes>
            </div>
        </div>
    );
}

export default App;
