import Navbar from "react-bootstrap/Navbar";
import React from "react";
import {Container, Nav, NavLink} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faVideoSlash} from "@fortawesome/free-solid-svg-icons/faVideoSlash";
import "../App.css";
import {useDispatch, useSelector} from "react-redux";
import {clearCurrentUser} from "../store/action/user";

export default function HorizontalMenu() {

    const currentUser = useSelector((state) => state.user);

    const isLoggedIn = currentUser?.token;
    const dispatch = useDispatch();

    return (
        <div className="horizontalMenu">
            <Navbar bg="dark" variant="dark">
                <Container fluid>
                    <Navbar.Brand href="/"
                                  style={{
                                      color: "gold"
                                      , marginLeft: "30px", marginRight: "40px"
                                  }}>
                        <FontAwesomeIcon icon={faVideoSlash}/>
                        Banking App
                    </Navbar.Brand>
                    <Nav style={{flex: "max-content"}}>
                        <NavLink className="nav-link" href="/nextTopic">
                            Next Topic
                        </NavLink>
                    </Nav>
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