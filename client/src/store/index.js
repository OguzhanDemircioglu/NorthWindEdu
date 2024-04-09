import { combineReducers, createStore } from "redux";
import userReducer from "./userMutations";

const rootReducer = combineReducers({
  user: userReducer,
});

const store = createStore(rootReducer);

export default store;
