import axios from "axios"

export const loginPlayer = (nickname, password) => axios.get(`/checkers/login?nickname=${nickname}&passwd=${password}`).then(responce => responce.data);

export const createPlayer = (nickname, password) => axios.request(`/checkers/createUser?nickname=${nickname}&passwd=${password}`).then(responce => responce.data);

// export const createPlayer = (nickname, password) => axios.post('api/user', {
//     nickname,
//     password
//   }).then(responce => responce.data);

export const getLoggedPlayer = () => axios.get(`/checkers/loggedUser`).then(responce => responce.data);

export const logoutPlayer = () => axios.request(`/checkers/logout`);

export const loadBestScores = () => axios.get(`/api/score/checkers`).then(responce => responce.data);
export const loadComments = () => axios.get(`/api/comment/checkers`).then(responce => responce.data);
export const loadAverageRating = () => axios.get(`/api/rating/checkers`).then(responce => responce.data);


export const postScore = async (player, points) => await axios.post('api/score', {
  player,
  game: "checkers",
  points,
  playedOn: new Date()
}, {
  headers: {
    'Content-Type': 'application/json'
  }
}
).then(function (response) {
  console.log(response);
})
.catch(function (error) {
  console.log(error);
});

export const postComment = async (player, comment) => await axios.post('api/comment', {
  player,
  game: "checkers",
  comment,
  commentedOn: new Date()
}, {
  headers: {
    'Content-Type': 'application/json'
  }
}
).then(function (response) {
  console.log(response);
})
.catch(function (error) {
  console.log(error);
});

export const postRating = async (player, rating) => await axios.post('api/rating', {
  player,
  game: "checkers",
  rating,
  ratedOn: new Date()
}, {
  headers: {
    'Content-Type': 'application/json'
  }
}
).then(function (response) {
  console.log(response);
})
.catch(function (error) {
  console.log(error);
});