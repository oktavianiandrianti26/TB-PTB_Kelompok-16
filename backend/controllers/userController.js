const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const userModel = require('../models/userModel');
const responseView = require('../views/responseView');
const isValidEmail = require('../utility/helper');  


exports.register = async (req, res) => {
  const { email, hp, password, name } = req.body;
  if (!email || !hp || !password || !name) return responseView.badRequest(res, 'email, hp, name, and password are required');

  if(!isValidEmail(email)) {
      return responseView.badRequest(res, 'Invalid email format');
    }

  try {
    const hashedPassword = await bcrypt.hash(password, 10);
    const checkUser = await userModel.findUserByEmail(email)
    if(checkUser){
      return responseView.badRequest(res, 'Email already taken!!');
    }

    const newUser = await userModel.createUser(email, hp, hashedPassword, name);
    responseView.success(res, 'User registered successfully', newUser);
  } catch (err) {
    responseView.error(res, 'Error registering user', err.message);
  }
};

exports.login = async (req, res) => {
  const { email, password } = req.body;
  if (!email || !password) return responseView.badRequest(res, 'email and password are required');

  try {
    const user = await userModel.findUserByEmail(email);
    if (!user) return responseView.badRequest(res, 'User not found');

    const isValidPassword = await bcrypt.compare(password, user.password);
    if (!isValidPassword) return responseView.badRequest(res, 'Invalid password');

    // const token = jwt.sign({ id: user.id, hp: user.hp }, 'secret_key', { expiresIn: '9999h' });
    // responseView.success(res, 'Login successful', { token });

    responseView.success(res, 'Login successful', { user });

  } catch (err) {
    responseView.error(res, 'Error logging in', err.message);
  }
};


exports.getUserProfile = async (req, res) => {
  const { hp } = req.query;
  console.log(hp);
  try {
    const user = await userModel.findUserByHp(hp);
    if (!user) return responseView.badRequest(res, 'User not found');

    if (user.image) {
      const baseUrl = `${req.protocol}://${req.get('host')}`; 
      user.image = `${baseUrl}/uploads/${user.image}`;
    }
    responseView.success(res, 'get User Profile Successfully', user);
  } catch (err) {
    responseView.error(res, 'Error logging in', err.message);
  }
};

exports.updateProfile = async (req, res) => {
  const { name, hp, address, image } = req.body;
  console.log(req.body);
  try {
    console.log(name);

    const user = await userModel.findUserByHp(hp);
    if (!user) return responseView.badRequest(res, 'User not found');

      user.name = name;
      user.hp = hp;
      user.address = address;
      user.image = 'tes.png';
      console.log("resul1t");
      const result = await userModel. updateProfile(hp, user);
      console.log("result");
      if(result){
        console.log("Success");
        responseView.success(res, 'Update profile Successfully', user);
      }else{
        throw "error when updating data";
      }


  } catch (err) {
    responseView.error(res, 'Error logging in', err.message);
  }
};