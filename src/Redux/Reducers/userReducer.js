import { GET_USER_BY_ID, ADD_USER, DELETE_USER, LOG_OUT_USER, CHECK_USER_BY_EMAIL, ADD_VIEWER } from "../Actions/userActions";

const initialState = {
  user: {},
};

const userReducer = (state = initialState, action) => {
  switch (action.type) {
    case GET_USER_BY_ID:
      return {
        ...state,
        user: action.payload,
      };

    case ADD_USER:
      return {
        ...state,
        user: action.payload,
      };

    case DELETE_USER:
      return {
        ...state,
        user: {},
      };

    case LOG_OUT_USER:
      return {
        ...state,
        user: {},
      };

    case CHECK_USER_BY_EMAIL:
      return {
        ...state,
        user: action.payload,
      };

    case ADD_VIEWER:
      return {
        ...state,
        user: {
          ...state.user,
          counts: [...state.user.counts.slice(0, 3), (state.user.counts[3] || 0) + 1, ...state.user.counts.slice(4)],
        },

      };

    default:
      return state;
  }
};

export default userReducer;


