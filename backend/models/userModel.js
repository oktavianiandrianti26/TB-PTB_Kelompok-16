const pool = require('../config/db');

exports.createUser = async (email, hp, password, name) => {
  const result = await pool.query(
    'INSERT INTO users (email, hp, password, name) VALUES ($1, $2, $3, $4) RETURNING *',
    [email, hp, password, name]
  );
  return result.rows[0];
};

exports.findUserByEmail = async (email) => {
  const result = await pool.query('SELECT * FROM users WHERE email = $1', [email]);
  return result.rows[0];
};

exports.findUserByHp = async (hp) => {
  const result = await pool.query('SELECT * FROM users WHERE hp = $1', [hp]);
  return result.rows[0];
};

exports.updateProfile = async (hp, newData) => {
  console.log(newData);
  const result = await pool.query('UPDATE users SET hp = $2, address = $3, image = $4, name = $5 WHERE id = $1', [newData.id, newData.hp, newData.address, newData.image, newData.name]);
  if(result.rowCount > 0){
    console.log("updated");
    return true;
  }else{
    return false;
  }
};