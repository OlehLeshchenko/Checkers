<template>
    <div class="container">
        <div class="auth-block">
            <div class="form-container sign-up">
                <div class="form">
                    <h1>Create Account</h1>
                    <input required class="input nickname-input" type="text" placeholder="Nickname" v-model="signUpNickname">
                    <input required class="input password-input" type="password" placeholder="Password" v-model="signUpPassword">
                    <input required class="input repeat-input" type="password" placeholder="Repeat the password" v-model="signUpRepeatPassword">
                    <a v-if="showElement" @click="changeMode" class="forget-input" to="/">Do you have an account? <i>Log in</i></a>
                    <div class="error">{{ error }}</div>
                    <button @click = "signUp" class = "button">Sign Up</button>
                </div>
            </div>
            <div class="form-container sign-in">
                <div class="form">
                    <h1>Sign In</h1>
                    <input required class="input nickname-input" type="text" placeholder="Nickname" v-model="signInNickname">
                    <input required class="input password-input" type="password" placeholder="Password" v-model="signInPassword">
                    <a class="forget" to="/">Forget Your Password?</a>
                    <a v-if="showElement" @click="changeMode" class="forget-input" to="/">Don't have an account? <i>Create</i></a>
                    <div class="error">{{ error }}</div>
                    <button @click = "signIn" class="button">Sign In</button>
                    <button @click = "navigateTo('/game')" class="button guest-button">Guest</button>
                </div>
            </div>
            <div class="toggle-container">
                <div class="toggle">
                    <div class="toggle-panel toggle-left">
                        <h1>Ready to return?</h1>
                        <div class="toggle-text">Dive into the timeless classic of strategy and skill. Ready to claim the title of checkers champion?<br/>Join the game now!</div>
                        <button @click="removeActiveClass" class="button hidden">Sign In</button>
                    </div>
                    <div class="toggle-panel toggle-right">
                        <h1>Welcome to Checkers!</h1>
                        <div class="toggle-text">Whether seasoned or new, sharpen your tactics, anticipate moves, and aim for victory!</div>
                        <button @click="addActiveClass" class="button hidden">Sign Up</button>
                    </div>
                </div>
            </div>    
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return{
            authBlock: undefined,
            showElement: true,
            signInNickname: "",
            signInPassword: "",
            signUpNickname: "",
            signUpPassword: "",
            signUpRepeatPassword: "",
            error: ""
        }
    },
    async created(){
        if(await getLoggedPlayer()) return navigateTo('/game');
    },
	mounted(){
		this.authBlock = document.querySelector('.auth-block');
        this.checkWindowWidth();
        window.addEventListener('resize', this.checkWindowWidth); 
	},
    methods: {
        addActiveClass(){
            this.error = "";
            this.authBlock?.classList.add("active");
        },
        removeActiveClass(){
            this.error = "";
            this.authBlock?.classList.remove("active");
        },
        async signIn(){
            if(!this.isNicknameValid(this.signInNickname) || !this.isPasswordValid(this.signInPassword)) return;
            const result = loginPlayer(this.signInNickname, this.signInPassword);
            if(await result) return navigateTo('/game');
            else{
                this.error = "The nickname or password is incorrect"
            }
        },
        async signUp(){
            if(!this.isNicknameValid(this.signUpNickname) || !this.isPasswordValid(this.signUpPassword)) return;
            else if(this.signUpPassword !== this.signUpRepeatPassword){
                this.error = "Passwords are not the same!";
                retur;
            }
            const result = createPlayer(this.signUpNickname, this.signUpPassword);
            if(await result) return navigateTo('/game');
            else{
                this.error = "This nickname is already taken!"
            }
        },
        changeMode(){
            this.error = "";
            this.authBlock?.classList.toggle("active");
        },
        checkWindowWidth() {
            const windowWidth = window.innerWidth;
            this.showElement = windowWidth < 768;
        },
        containsWhitespace(str){
           return /\s/.test(str)
        },
        isNicknameValid(nickname){
            if(this.containsWhitespace(nickname)){
                this.error = "The nickname field must not contain spaces!"
                return false;
            }
            else if(nickname.length === 0){
                this.error = "The nickname field is empty!"
                return false;
            }
            else if(/^\d/.test(nickname.charAt(0))){
                this.error = "The nickname cannot begin with a number!'"
                return false;
            }
            else if(nickname.length < 4){
                this.error = "The nickname must have at least 4 characters!"
                return false;
            }
            this.error = ""
            return true;

        },
        isPasswordValid(password){
            if(this.containsWhitespace(password)){
                this.error = "The password field must not contain spaces!"
                return false;
            }
            else if(password.length === 0){
                this.error = "The password field is empty!"
                return false;
            }
            else if(password.length < 4){
                this.error = "The password must have at least 4 characters!"
                return false;
            }
            this.error = ""
            return true;
        }

    },
}
</script>

<style>
@import 'normalize.css/normalize.css';
@import url('https://fonts.googleapis.com/css2?family=Montserrat:ital,wght@0,100..900;1,100..900&display=swap');
* {
	margin: 0;
	box-sizing: border-box;
	font-family: 'Montserrat', sans-serif;
}
body{
    min-height: 100vh;
    display: flex;
    flex-direction: column;
	background: url("/background.jpg");
    background-position: center; 
    background-repeat: no-repeat; 
    background-size: cover; 
}
.content-table {
    background: #cccccc;
    border-collapse: collapse;
    margin: 25px 0;
    font-size: 18px;
    min-width: 400px;
    border-radius: 5px 5px 0 0;
    overflow: hidden;
    box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
    width: 80%;
    max-width: 1200px;
}

tr>td:first-child, th:first-child{
  text-align: center;
  font-weight: bold;
}

.content-table thead tr {
    background-color: #009879;
    color: #ffffff;
    text-align: left;
    font-weight: bold;
}

.content-table th,
.content-table td {
    padding: 12px 15px;
}

.content-table tbody tr {
    border-bottom: 1px solid #dddddd;
}

.content-table tbody tr:nth-of-type(odd) {
    background-color: #f3f3f3;
}

.content-table tbody tr:last-of-type {
    border-bottom: 2px solid #009879;
}

.content-table tbody tr:hover {
    font-weight: bold;
    color: #009879;
}
</style>

<style scoped>
.error{
    color: red;
    text-align: center;
    font-size: 13px;
}
.toggle-container h1{
    margin-bottom: 0;
}

.container{
    min-height: 100vh;
    margin: 0;
    display: flex;
    align-items: center;
    justify-content: center;
}
a{
    cursor: pointer;
}
.auth-block{
    background-color: #fff;
    border-radius: 30px;
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.35);
    position: relative;
    overflow: hidden;
    width: 768px;
    max-width: 90%;
    min-height: 480px;
}

.auth-block .toggle-text{
    font-size: 14px;
    line-height: 20px;
    letter-spacing: 0.3px;
    margin: 20px 0;
}

.auth-block a{
    color: #333;
    font-size: 13px;
    text-decoration: none;
    margin: 15px 0 10px;
}

.button{
    background-color: #5cd1b8;
    color: rgb(31 31 31);
    font-size: 12px;
    padding: 10px 45px;
    border: 1px solid transparent;
    border-radius: 8px;
    font-weight: 600;
    letter-spacing: 0.5px;
    text-transform: uppercase;
    margin-top: 10px;
    cursor: pointer;
}

.auth-block .button.hidden{
    background-color: transparent;
    border-color: rgb(31 31 31);
}

.auth-block .form{
    background-color: #fff;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
    padding: 0 40px;
    height: 100%;
}

.auth-block .input{
    background-color: #eee;
    border: none;
    margin: 8px 0;
    padding: 10px 15px;
    font-size: 13px;
    border-radius: 8px;
    width: 100%;
    outline: none;
}

.form-container{
    position: absolute;
    top: 0;
    height: 100%;
    transition: all 0.6s ease-in-out;
}

.sign-in{
    left: 0;
    width: 50%;
    z-index: 2;
}

.auth-block.active .sign-in{
    transform: translateX(100%);
}

.sign-up{
    left: 0;
    width: 50%;
    opacity: 0;
    z-index: 1;
}

.auth-block.active .sign-up{
    transform: translateX(100%);
    opacity: 1;
    z-index: 5;
    animation: move 0.6s;
}

@keyframes move{
    0%, 49.99%{
        opacity: 0;
        z-index: 1;
    }
    50%, 100%{
        opacity: 1;
        z-index: 5;
    }
}

.toggle-container{
    position: absolute;
    top: 0;
    left: 50%;
    width: 50%;
    height: 100%;
    overflow: hidden;
    transition: all 0.6s ease-in-out;
    border-radius: 70px 0 0 140px;
    z-index: 1000;
}

.auth-block.active .toggle-container{
    transform: translateX(-100%);
    border-radius: 0 70px 140px 0;
}

.toggle{
    height: 100%;
    background: linear-gradient(25deg, rgba(35,172,143,1) 0%, rgba(20,218,177,1) 52%, rgba(8,233,187,1) 100%);
    color: black;
    position: relative;
    left: -100%;
    width: 200%;
    transform: translateX(0);
    transition: all 0.6s ease-in-out;
}

.auth-block.active .toggle{
    transform: translateX(50%);
}

.toggle-panel{
    position: absolute;
    width: 50%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
    padding: 0 30px;
    text-align: center;
    top: 0;
    transform: translateX(0);
    transition: all 0.6s ease-in-out;
}

.toggle-left{
    transform: translateX(-200%);
}

.auth-block.active .toggle-left{
    transform: translateX(0);
}

.toggle-right{
    right: 0;
    transform: translateX(0);
}

.auth-block.active .toggle-right{
    transform: translateX(200%);
}

.guest-button{
    margin-top: 20px;
}

@media only screen and (max-width: 770px) {
    .toggle-container {
        display: none;
    } 
    .auth-block{
        min-width: 250px;
        width: 90%;
        max-width: 384px;
    }
    .form-container{
        transition: 0s;
        width: 100%;
    }
    .auth-block.active .sign-in{
        transform: translateX(0);
    }
    .auth-block.active .sign-up{
        transform: translateX(0);
    }
    .auth-block.active .sign-up{
        animation: none;
    }
}
</style>