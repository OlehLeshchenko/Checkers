<template>
    <div v-if="isLoggingOut" class="message-wrapper" login="{{ login }}">
		<div class="message">
			<div class="message-text">
				Do you really want to log out of your account?			
			</div>
			<img src="/icons/logout.png" alt="" class="message-photo">
			<div class="message-buttons">
				<button @click="this.logout()" class="end-button">YES</button>
				<button @click="isLoggingOut=false" class="end-button">NO</button>
			</div>
		</div>
	</div>
    <div class="header">
      <NuxtLink class="logo" to="/service"><img src="/icons/logo.gif" alt="logo" class="logo"></NuxtLink>
      <div @click="isLoggingOut=true" class = "login" v-if="login">Logged as<span>: <span class="dec-text">{{login}}</span></span></div>
      <NuxtLink to="/"class = "login" v-else>User not logged in<span>. <span class="dec-text">Log in?</span></span></NuxtLink>

      <img @click="isLoggingOut=true" class = "small-login" v-if="login" src="/icons/logout_small.png" alt=""/>
      <NuxtLink to="/"class = "small-login" v-else><img src="/icons/login.png" alt=""/></NuxtLink>

    </div>
</template>

<script>
export default {
    data() {
        return{
            login: "",
            isLoggingOut: false
        }
    },
    async created(){
        this.login = await getLoggedPlayer();
        console.log(this.login);
    },
    methods: {
        logout(){
			logoutPlayer().then(()=>{
                this.isLoggingOut = false;
                this.login = "";
            });
		}
    }
}
</script>

<style>
.header{
    position: absolute;
    top: 0;
    left: 0;
    display: flex;
    width: 100%;
    height: 80px;
    background-color: rgba(0, 152, 122, 0.5);
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1),
        inset 0px 0px 36px 0px rgba(0,0,0,0.1);
    align-items: center;
    padding: 0 50px;
    justify-content: space-between;
}
.logo{
    display: block;
    max-width: 200px;
}
.dec-text{
    font-weight: bold;
}
.login{
    display: flex;
    text-decoration: none;
    cursor: pointer;
    color: black;
    font-size: 18px;
}

.message-wrapper{
	z-index: 1010;
	position: absolute;
	width: 100%;
	height: 100vh;
	background-color: #37bda063;
	display: flex;
	justify-content: center;
	align-items: center;
}

.message{
	padding: 50px;
	border-radius: 5px;
	background-color: #FFFFFF;
	box-shadow: 0 5px 15px rgba(0, 0, 0, 0.35);
	margin: 0 20px;
}

.message-text{
	font-size: 25px;
    text-align: center;
}

.message-buttons button{
	margin: 0 20px;
}

.message-photo{
	display: block;
	margin: auto;
	height: 250px;
	margin-top: 30px;
}

.message-buttons{
	margin-top: 30px;
	width: 100%;
	display: flex;
	justify-content: space-between;
    flex-wrap: wrap;
}

.end-button{
	margin-top: 40px;
	background-color: #5cd1b8;
    color: rgb(31 31 31);
    font-size: 20px;
    padding: 10px 45px;
	box-shadow: 0 5px 15px rgba(0, 0, 0, 0.35);
	border: 1px solid transparent;
    border-radius: 8px;
    font-weight: 600;
    cursor: pointer;
}

.small-login{
    display: none;
}

@media only screen and (max-width: 480px) {
    .message-photo{
        width: 150px;
        height: 150px;
    }
    .message-buttons{
        justify-content: center;
        gap: 15px;
    }
}

@media only screen and (max-width: 550px) {
    .login{
        display: none;
    }
    .small-login{
        display: block;
    }
}
</style>