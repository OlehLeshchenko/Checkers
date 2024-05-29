<template>
  <base-header />
  <div class="container">
	
	<div class="btn-group"><button @click="navigateTo('/game')" class="btn submit" id="return">Return to game</button></div>
	<div class="wrapper">
      <h3>Leave your feedback here</h3>
      <div>
        <div class="rating">
          <input v-model="playerRating" type="number" name="rating" hidden />
          <i class="bx bx-star star" style="--i: 0"></i>
          <i class="bx bx-star star" style="--i: 1"></i>
          <i class="bx bx-star star" style="--i: 2"></i>
          <i class="bx bx-star star" style="--i: 3"></i>
          <i class="bx bx-star star" style="--i: 4"></i>
        </div>
        <textarea
		  v-model = "playerComment"
          name="opinion"
          cols="30"
          rows="5"
          placeholder="Your opinion..."
        ></textarea>
		<div class="error">{{ error }}</div>
        <div class="btn-group">
          <button @click="publishReview" type="submit" class="btn submit">Submit</button>
          <button @click="clearFields" class="btn cancel">Cancel</button>
        </div>
      </div>
    </div>

	<hr class="hr"/>

	<div class="wrapper"><h2>Rating: {{ gameRating }}</h2></div>

	<hr class="hr"/>
    <table class="content-table scores">
      <thead>
        <tr>
          <th>Rank</th>
          <th>Player</th>
          <th>Points</th>
          <th>Date</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(score, idx) in theBestScores">
          <td>{{ idx + 1 }}</td>
          <td>{{ score.player }}</td>
          <td>{{ score.points }}</td>
          <td>{{ formatedDate(score.playedOn) }}</td>
        </tr>
        <tr v-if="theBestScores.length == 0">
          <td></td>
          <td></td>
          <td></td>
          <td></td>
        </tr>
      </tbody>
    </table>

	<hr class="hr"/>

    <table class="content-table comments">
      <thead>
        <tr>
          <th>Player</th>
          <th>Date</th>
          <th>Comment</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="comment in comments">
          <td>{{ comment.player }}</td>
          <td>{{ formatedDate(comment.commentedOn) }}</td>
          <td>{{ comment.comment }}</td>
        </tr>
        <tr v-if="theBestScores.length == 0">
          <td></td>
          <td></td>
          <td></td>
        </tr>
      </tbody>
    </table>


  </div>
</template>

<script>
export default {
  data() {
	return {
		login: "",
		theBestScores: [],
		gameRating: 0,
		playerComment: "",
		comments: [],
		dateFormating: {
			year: "numeric",
			month: "long",
			day: "numeric",
			hour: "numeric",
			minute: "numeric",
			hour12: false,
		},
		error: ""
    };
  },
  async created() {
    this.theBestScores = await loadBestScores();
    this.comments = await loadComments();
	this.gameRating = await loadAverageRating();
	const log = await getLoggedPlayer();
	this.login = log ? log : "";
  },
  mounted(){
	const allStar = document.querySelectorAll('.rating .star')
	allStar.forEach((item, idx)=> {
		item.addEventListener('click', function () {
			let click = 0
			allStar.forEach(i=> {
				i.classList.replace('bxs-star', 'bx-star')
				i.classList.remove('active')
			})
			for(let i=0; i<allStar.length; i++) {
				if(i <= idx) {
					allStar[i].classList.replace('bx-star', 'bxs-star')
					allStar[i].classList.add('active')
				} else {
					allStar[i].style.setProperty('--i', click)
					click++
				}
			}
		})
	})
  },
  methods: {
    formatedDate(date) {
      return new Date(date).toLocaleDateString("en-US", this.dateFormating);
    },
	clearFields(){
		this.playerComment = "";
		this.error = "";
		document.querySelectorAll('.star').forEach(star =>{
			star.classList.replace('bxs-star', 'bx-star');
			star.classList.remove("active");
		});
	},
	getPlayerRating(){
		return document.querySelectorAll('.bxs-star').length;
	},
	async publishReview(){
		if(this.login == ""){
			console.log(123213);
			this.error = "Non-logged-in users cannot leave comments or rate the game";
			return;
		}
		if(this.getPlayerRating() != 0){
			postRating(this.login, this.getPlayerRating()).then(() =>{
				clearFields();
				loadAverageRating().then(data => this.gameRating = data);
			});
		}
		if(this.playerComment.trim().length !== 0){
			postComment(this.login, this.playerComment.trim()).then(() =>{
				loadComments().then(data => this.comments = data);
			});
		}
		this.clearFields();
	}
  },
};
</script>

<style scoped>
.container {
    width: 100%;
    min-height: 100vh;
    display: flex;
    justify-content: center;
    align-items: center;
    padding-top: 100px;
    flex-direction: column;
}

.error{
	margin: 30px 0;
	color: red;
}
.wrapper {
	margin-top: 60px;
	background: #FFF;
	padding: 2rem;
	max-width: 576px;
	width: 90%;
	border-radius: .75rem;
	box-shadow: 8px 8px 30px rgba(0,0,0,.05);
	text-align: center;
}
.wrapper h3 {
	font-size: 1.5rem;
	font-weight: 600;
	margin-bottom: 1rem;
}
.rating {
	display: flex;
	justify-content: center;
	align-items: center;
	grid-gap: .5rem;
	font-size: 2rem;
	color: #FFBD13;
	margin-bottom: 2rem;
}
.rating .star {
	cursor: pointer;
}
.rating .star.active {
	opacity: 0;
	animation: animate .5s calc(var(--i) * .1s) ease-in-out forwards;
}

@keyframes animate {
	0% {
		opacity: 0;
		transform: scale(1);
	}
	50% {
		opacity: 1;
		transform: scale(1.2);
	}
	100% {
		opacity: 1;
		transform: scale(1);
	}
}


.rating .star:hover {
	transform: scale(1.1);
}
textarea {
	width: 100%;
	background: #F5F5F5;
	padding: 1rem;
	border-radius: .5rem;
	border: none;
	outline: none;
	resize: none;
	margin-bottom: .5rem;
}
.btn-group {
	display: flex;
	grid-gap: .5rem;
	align-items: center;
}
.btn-group .btn {
	padding: .75rem 1rem;
	border-radius: .5rem;
	border: none;
	outline: none;
	cursor: pointer;
	font-size: .875rem;
	font-weight: 500;
}
.btn-group .btn.submit {
	background: #009879;
	color: #FFF;
}
.btn-group .btn.submit:hover {
	background: #009879;
}
.btn-group .btn.cancel {
	background: #FFF;
	color: #009879;
}
.btn-group .btn.cancel:hover {
	background: #F5F5F5;
}
.hr{
	width: 80%;
	height: 5px;
	background-color: #009879;
	margin: 30px auto;
	border: none;
}
#return{
	margin-top: 60px;
	font-size: 25px;
}
</style>
