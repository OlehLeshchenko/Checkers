<template>
	<base-header/>
	<div class="container">

		<div v-if="state ==='PLAYING'" class="field-border">
			<div class="field">
				<template
					v-for = "(row, rowIdx) in this.tiles"
				>
					<template
					v-for = " (tile, colIdx) in row">
						<div 
						@click = "processInput(tile)"
						class="tile"
						:id="`tile-${tile.posY}${tile.posX}`"
						:class = "{
							'brown' : (rowIdx + colIdx) % 2 !== 0,
							'beige' : (rowIdx + colIdx) % 2 === 0
						}"
						>
							<img 
								v-if="tile.type != 'Tile'"
								class="piece"
								:src = "`/pieces/${tile.color.toLowerCase()}_${tile.type.toLowerCase()}.svg`"
							/>
						</div>
					</template>
				</template>
			</div>
		</div>
		<div class="endgame" v-else-if="state == 'WHITE_WON'">
			<h1 class="end-title">WHITE WON!</h1>
			<div class="gamepoints">
				<br/>
				<br/>
				Amount of commoners: {{field.scores.commonersCount}} --> {{field.scores.commonerPoints}}<br/><br/>
				Amount of kings: {{field.scores.kingsCount}} --> {{field.scores.kingPoints}}<br/><br/>
				Amount of minutes:      {{field.scores.minutes}} --> {{field.scores.timePoints}}<br/><br/>
				<strong>Total score:</strong>         {{totalScore}}<br/>
				<div v-if="!login" class="error">The result will not be saved for unauthorized users</div>
			</div>
			<button @click="createNewGame" class="end-button">Start a new game?</button>
		</div>
		<div class="endgame" v-else>
			<h1 class="end-title">BLACK WON!</h1>
			<div class="end-text">You will definitely win next time</div>
			<button @click="createNewGame" class="end-button">Start a new game?</button>
		</div>
	</div>
</template>

<script>
export default {
    data() {
        return{
			login: "",
            field: [],
			selectedTile : "",
			capturingPieces: [],
			state: 'PLAYING',
			theBestScores: [],
			comments: [],
			dateFormating : {
				year: 'numeric',
				month: 'long',
				day: 'numeric',
				hour: 'numeric',
				minute: 'numeric',
				hour12: false 
			}
        }
    },
	async created(){
		this.field = await loadField();
		this.state = this.gamestate;
		this.capturingPieces = await loadCapturingPieces();
		this.showCapturingPieces(this.capturingPieces);
		this.theBestScores = await loadBestScores();
		this.comments = await loadComments();
		const log = await getLoggedPlayer();
		this.login = log ? log : "";
  	},
	computed: {
		tiles() {
			return this.field && this.field.tiles ? this.field.tiles : [];
		},
		currentPlayer(){
			return this.field.currentPlayer;
		},
		gamestate(){
			return this.field.state;
		},
		bot(){
			return this.field.bot.active;
		},
		totalScore(){
			return this.field.scores.commonerPoints + this.field.scores.kingPoints + this.field.scores.timePoints;
		}
	},
    methods: {
		async createNewGame(){
			this.field = await loadNewGameField();
			this.state = this.gamestate;
			this.capturingPieces = await loadCapturingPieces()
			this.showCapturingPieces(this.capturingPieces);
        }
        ,
		async processInput(tile) {
			this.hidePossibleMoves();
			this.showCapturingPieces(this.capturingPieces);
			if(this.selectedTile && tile?.color !== this.currentPlayer ){ 
			 	const possibleMoves = await loadPossibleMoves(this.selectedTile);
				if(possibleMoves.map(move => move.to).some(to => to.posX === tile.posX && to.posY === tile.posY)){
					this.processMove(tile);
				}
				this.selectedTile = "";
			}
			else {
				if(tile?.color !== this.currentPlayer) return;
				this.showPossibleMoves(tile);		
				this.selectedTile = tile
			}
		},
		processMove(tile){
			makeMove(this.selectedTile, tile).then(field => {
				this.showCapturingPieces([]);
				this.field = field
				console.log("asdasdasd");
			})
			.then(() =>  {
				if(this.bot && this.currentPlayer === "BLACK") {
					console.log("asdasdasd");
					setTimeout(() => {
						makeBotMove().then(field =>this.field = field).then(() =>{
							loadCapturingPieces().then(pieces => {
								this.capturingPieces = pieces;
								this.showCapturingPieces(pieces);
							});
						});
					}, 500);
				}
				else {
					loadCapturingPieces().then(pieces => {
						this.capturingPieces = pieces;
						this.showCapturingPieces(pieces);
					});
				}

			});
		},
		async showPossibleMoves(tile){	
			const moves = await loadPossibleMoves(tile);
			moves?.forEach(move => {
				const tile = document.querySelector(`#tile-${move.to.posY}${move.to.posX}`);
				tile.classList.add("possible-move");
				tile.classList.remove("disabled-tile");
			});
		},
		hidePossibleMoves(){
			document.querySelectorAll(".tile").forEach(tile => {
				tile.classList.remove("possible-move");
			});
		},
		async showCapturingPieces(capturingPieces){
			if(capturingPieces.length > 0){
				document.querySelectorAll(".tile").forEach(tile => tile.classList.add("disabled-tile"));
				capturingPieces.forEach(piece =>{
					document.querySelector(`#tile-${piece.posY}${piece.posX}, .possible-move`).classList.remove("disabled-tile");
				});
			} 
			else{
				document.querySelectorAll(".disabled-tile").forEach(tile =>{
					tile.classList.remove("disabled-tile");
				});
			}
		},
		formatedDate(date){
			return new Date(date).toLocaleDateString('en-US', this.dateFormating)
		}
    },
	watch: {
		async gamestate(){
			if(this.gamestate !== 'PLAYING'){
				if(this.gamestate === 'WHITE_WON'){
					getLoggedPlayer().then(player => {
						if(player){
							postScore(player, this.totalScore).then(() => {
								loadBestScores().then(data => this.theBestScores = data);
							});
						}
					});
				}
				setTimeout(()=>{
					this.state = this.gamestate;
				}, 1500);
			}
		}

    },

}   

</script>

<style scoped>
.container{
	width: 100%;
	min-height: 100vh;
	display: flex;
	justify-content: center;
	align-items: center;
	padding-top: 100px;
	flex-direction: column;
}
.field-border{
	width: 95%;
	max-width: 750px;
	aspect-ratio: 1 / 1;
	display: flex;
	align-items: center;
	justify-content: center;
	background-color: #58392f;
	box-shadow: 10px 10px 78px 0px rgba(0,0,0,0.44),
	inset 0px -11px 78px 0px rgba(0,0,0,0.3);

}
.field{
	width: 94%;
	max-width: 705px;
	aspect-ratio: 1 / 1;
	display: flex;
	flex-wrap: wrap;
	
}
.tile{
	position: relative;
	width: 12.5%;
	height: 12.5%;
	display: flex;
	justify-content: center;
	align-items: center;
}
.beige{
	background-color: #e8d1b9;
}
.brown{
	background-color: #9d6b5c;
}
.piece{
	display: block;
	width: 90%;
	height: 90%;
}
.possible-move{
	background-color: #a46dd1;
}
.disabled-tile{
	filter: brightness(45%);
}
.endgame{
	background-color: #fff;
    border-radius: 30px;
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.35);
    width: 768px;
    max-width: 90%;
	display: flex;
	flex-direction: column;
	align-items: center;
	padding: 50px 30px;
	margin-bottom: 100px;
}
.end-title{
	margin: 0;
	text-align: center;
	font-size: 60px;
	color: #3b2626;
}
.end-text{
	margin-top: 50px;
	font-size: 16px;
	font-style: italic;
}
.error{
	margin-top: 30px;
	color: red;
}
</style>