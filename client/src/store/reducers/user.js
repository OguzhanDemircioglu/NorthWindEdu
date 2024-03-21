import { CLEAR_CURRENT_USER, SET_CURRENT_USER } from "../types";

const userReducer = (state = {}, action) => {
  switch (action?.type) {
    case SET_CURRENT_USER:
      localStorage.setItem("current_user", JSON.stringify(action?.payload));
      return action?.payload;
    case CLEAR_CURRENT_USER:
      localStorage.removeItem("current_user");
      return null;
    default:
      return JSON.parse(localStorage.getItem("current_user"));
  }
};

export default userReducer;
