import Navbar from "react-bootstrap/Navbar";
import React from "react";
import {Container, Nav, NavLink} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faWind} from "@fortawesome/free-solid-svg-icons/faWind";
import "../App.css";
import {useDispatch, useSelector} from "react-redux";
import {clearCurrentUser} from "../store/actions";

export default function HorizontalMenu() {

    const currentUser = useSelector((state) => state.user);

    const isLoggedIn = currentUser?.token;
    const dispatch = useDispatch();

    return (
        <div className="horizontalMenu">
            <Navbar bg="dark" variant="dark">
                <Container fluid>
                    <Navbar.Brand
                        href="/"
                        style={{
                            color: "gold"
                            , marginLeft: "30px", marginRight: "40px"
                        }}>
                        <FontAwesomeIcon icon={faWind}/>
                        &nbsp;NorthWind
                    </Navbar.Brand>
                    <Nav style={{flex: "max-content"}}>
                        {isLoggedIn && (
                            <>
                                <NavLink className="nav-link" href="/customers">
                                    Customers
                                </NavLink>
                                <NavLink className="nav-link" href="/employees">
                                    Employees
                                </NavLink>
                                <NavLink className="nav-link" href="/orders">
                                    Orders
                                </NavLink>
                            </>
                        )}
                    </Nav>
                    {currentUser?.role === 'ADMIN' &&
                        <NavLink className="nav-link" style={{color: "cyan",marginRight:"10px"}} href="/adminConsole">
                            AdminConsole
                        </NavLink>}
                    {isLoggedIn ?
                        <NavLink className="nav-link" style={{color: "cyan"}} href="/login"
                                 onClick={() => dispatch(clearCurrentUser())}>
                            LogOut
                        </NavLink>
                        :
                        <NavLink className="nav-link" style={{color: "cyan"}} href="/login">
                            Login
                        </NavLink>}
                </Container>
            </Navbar>
        </div>
    );
};