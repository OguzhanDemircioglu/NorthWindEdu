import React, {useEffect, useState} from 'react';
import {Button, Input} from "@mui/material";
import "../App.css"
import {USER_ROLE} from "../store/Enums";
import AuthService from "../services/AuthService";

const AdminConsole = () => {

    const [items, setItems] = useState(null);
    const [searchKey, setSearchKey] = useState(null);
    const [editID, setEditID] = useState(null);

    const [formData, setFormData] = useState({
        id: '',
        username: '',
        password:'',
        email: '',
        role: ''
    });
    const [formUpdateData, setFormUpdateData] = useState({
        id: '',
        username: '',
        password:'',
        email: '',
        role: ''
    });

    useEffect(() => {
        AuthService.findAllUsers((responseData) => setItems(responseData))
    }, []);

    function insertUser(e) {
        e.preventDefault();
        if (formData.username === '' ||
            formData.password === '' ||
            formData.role === '' ||
            formData.email === '') {
            alert("Alanların hepsi dolu olmalı")
            return;
        }
        AuthService.insertUser(formData);
        window.location.reload();
    }

    function deleteUserById(id) {
        AuthService.deleteUserById(id);
        window.location.reload();
    }

    function updateUser() {
        formUpdateData.id = editID;

        AuthService.updateUser(formUpdateData);
        window.location.reload();
    }

    const handleInsert = (e) => {
        const {name, value} = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleUpdate = (e) => {
        const {name, value} = e.target;
        setFormUpdateData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    return (
        <div className="example">
            <Input type="text" placeholder="SearchByUserName..."
                   onChange={(e) => setSearchKey(e.target.value.toLowerCase())}
            />
            <table id="table1">
                <thead>
                <tr>
                    <th></th>
                    <th>username</th>
                    <th>password</th>
                    <th>email</th>
                    <th>createdAt</th>
                    <th>updatedAt</th>
                    <th>role</th>
                    <th>ADD & DELETE</th>
                    <th>UPDATE</th>
                </tr>
                </thead>
                <tbody>
                {items?.filter(i => !searchKey || i.username.includes(searchKey)).map((item, index) => {
                        return (
                            editID === item.id ?
                                <tr>
                                    <td>{index + 1}</td>
                                    <td>
                                        <input
                                            type="text"
                                            name="username"
                                            value={formUpdateData.username}
                                            onChange={handleUpdate}
                                            placeholder="Username"
                                        />
                                    </td>

                                    <td>
                                        <input
                                            type="text"
                                            name="password"
                                            value={formUpdateData.password}
                                            onChange={handleUpdate}
                                            placeholder="Password"
                                        />
                                    </td>

                                    <td>
                                        <input
                                            type="text"
                                            name="email"
                                            value={formUpdateData.email}
                                            onChange={handleUpdate}
                                            placeholder="Email"
                                        />
                                    </td>
                                    <td>
                                        <select name="role" value={formUpdateData.role}
                                                onChange={handleUpdate}>
                                            <option value="">Select Role</option>
                                            {Object.keys(USER_ROLE).map(key => USER_ROLE[key]).map(role => (
                                                <option key={role}
                                                        value={role}>{role}</option>
                                            ))}
                                        </select>
                                    </td>
                                    {item.createdAt ?
                                        <td>{item.createdAt[2]}
                                            .{item.createdAt[1]}
                                            .{item.createdAt[0]}
                                        </td> :
                                        <td/>
                                    }
                                    {item.updatedAt ?
                                        <td>{item.updatedAt[2]}
                                            .{item.updatedAt[1]}
                                            .{item.updatedAt[0]}
                                        </td> :
                                        <td/>
                                    }
                                    <td>
                                        <Button onClick={() => setEditID(null)}>CANCEL</Button>
                                    </td>
                                    <td>
                                        <Button onClick={updateUser}>SUBMIT</Button>
                                    </td>
                                </tr>
                                :
                                <tr key={index}>
                                    <td>{index + 1}</td>
                                    <td>{item.username}</td>
                                    <td/>
                                    <td>{item.email}</td>
                                    {item.createdAt ?
                                        <td>{item.createdAt[2]}
                                            .{item.createdAt[1]}
                                            .{item.createdAt[0]}
                                        </td> :
                                        <td/>
                                    }
                                    {item.updatedAt ?
                                        <td>{item.updatedAt[2]}
                                            .{item.updatedAt[1]}
                                            .{item.updatedAt[0]}
                                        </td> :
                                        <td/>
                                    }
                                    <td>{item.role}</td>
                                    <td>
                                        <Button onClick={() => deleteUserById(item.id)}>DELETE</Button>
                                    </td>
                                    <td>
                                        <Button onClick={() => setEditID(item.id)}>UPDATE</Button>
                                    </td>
                                </tr>
                        )
                    })
                }
                <tr>
                    <td>{!items ? 1 : items.length + 1}</td>
                    <td>
                        <input
                            type="text"
                            name="username"
                            value={formData.username}
                            onChange={handleInsert}
                            placeholder="Username"
                        />
                    </td>
                    <td>
                        <input
                            type="text"
                            name="password"
                            value={formData.password}
                            onChange={handleInsert}
                            placeholder="Password"
                        />
                    </td>
                    <td>
                        <input
                            type="text"
                            name="email"
                            value={formData.email}
                            onChange={handleInsert}
                            placeholder="Email"
                        />
                    </td>
                    <td/>
                    <td/>
                    <td>
                        <select name="role" value={formData.role}
                                onChange={handleInsert}>
                            <option value="">Select Role</option>
                            {Object.keys(USER_ROLE).map(key => USER_ROLE[key]).map(role => (
                                <option key={role}
                                        value={role}>{role}</option>
                            ))}
                        </select>
                    </td>
                    <td>
                        <Button type={"submit"} onClick={insertUser}>ADD</Button>
                    </td>
                    <td/>
                </tr>
                </tbody>
            </table>
        </div>
    );
};


export default AdminConsole;