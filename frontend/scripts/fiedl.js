import axios from "axios"

export const loadNewGameField = () => axios.get("/checkers/newgame?bot=1").then(responce => responce.data);

export const loadField = () => axios.get("/checkers/field").then(responce => responce.data);

export const loadPossibleMoves = async (tile) => await axios.get(`/checkers/moves?row=${tile.posY}&column=${tile.posX}`).then(response => response.data);

export const makeMove = (from, to) =>  axios.get(`/checkers/makemove?rowFrom=${from.posY}&columnFrom=${from.posX}&rowTo=${to.posY}&columnTo=${to.posX}`).
    then(response => response.data);
    
export const makeBotMove = () =>  axios.get(`/checkers/makeBotMove`).then(response => response.data);

export const loadCapturingPieces = () => axios.get("/checkers/capturingpieces").then(responce => responce.data);


