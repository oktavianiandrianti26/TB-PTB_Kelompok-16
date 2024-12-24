const db = require('../config/db');  
const moment = require('moment');
const createPresensi = async ({ name, datetime, location, description, image, type, userId}) => {
  try {
    const [date, time] = datetime.split('T');
    const alreadyPresentQuery = `SELECT name from presensi where name = $1 and TO_CHAR(datetime, 'YYYY-MM-DD') = $2 LIMIT 1`;

    const resultCheck = await db.query(alreadyPresentQuery, [name, date]);

    if (resultCheck.rows.length > 0) {
        throw new Error('You have already Presence!!');
    }

    const query = `
      INSERT INTO presensi (name, datetime, location, description, image,type, user_id)
      VALUES ($1, $2, $3, $4, $5, $6, $7)
      RETURNING id, name, datetime, location, description, image, created_at;
    `;
    const values = [name, datetime, location, description, image, type, userId];

    const result = await db.query(query, values);
    return result.rows[0];  // Return the inserted record
  } catch (err) {
    throw new Error('Error inserting presensi: ' + err.message);
  }
};


const createIzin = async ({ name, datetime, location, description, image, type, userId}) => {
  try {
    const [date, time] = datetime.split('T');
    const alreadyPresentQuery = `SELECT name from presensi where user_id = $1 and TO_CHAR(datetime, 'YYYY-MM-DD') = $2 LIMIT 1`;
    const dateConvert = moment(date).format("YYYY-MM-DD");
    const resultCheck = await db.query(alreadyPresentQuery, [userId, dateConvert]);
    console.log(dateConvert);
    console.log(userId)
    console.log(date)
    if (resultCheck.rows.length > 0) {
        throw new Error('You have already Permit!!');
    }

    const query = `
      INSERT INTO presensi (name, datetime, description, type, user_id)
      VALUES ($1, $2, $3, $4, $5)
      RETURNING id, name, datetime, description, created_at;
    `;
    const values = [name, datetime, description, type, userId];

    const result = await db.query(query, values);
    return result.rows[0];  // Return the inserted record
  } catch (err) {
    throw new Error('Error inserting presensi: ' + err.message);
  }
};

const getPresensi = async ({ userId }) => {
    const result = await db.query('SELECT * FROM presensi WHERE user_id = $1', [userId]);
    return result.rows;
  };

const getPermit = async ({ userId, date}) => {
    console.log(userId + " " + date);
    const result = await db.query(`SELECT *, TO_CHAR(datetime, 'DD') as day FROM presensi WHERE user_id = $1 AND TO_CHAR(datetime, 'YYYY-MM') = $2 and type = '2'`, [userId,date]);
     const data =  {
        "countPermit": result.rowCount,
        "permitData": result.rows
      }
    // console.log(result);
    return data;
  };


const getMonthlyPresensi = async ({ userId, date}) => {
    console.log(userId + " " + date);
    const result = await db.query(`SELECT *, TO_CHAR(datetime, 'DD') as day FROM presensi WHERE user_id = $1 AND TO_CHAR(datetime, 'YYYY-MM') = $2`, [userId,date]);
    console.log(result);
    return result.rows;
  };

  const getReportPerformance = async ({ userId ,date}) => {
    console.log("DATA USER" + userId + date)
    const result = await db.query(`select description from presensi where user_id = $1 and to_char(datetime,'YYYY-MM-dd') = $2 and type = '1'
union
select mt.description from daily_task dt 
join master_task mt on mt.id = dt.task_id
where user_id = $1 and to_char(datetime,'YYYY-MM-dd') = $2`, [userId,date]);
     const data =  {
        "countReport": result.rowCount,
        "reportData": result.rows
      }
    return data;
  }

  module.exports = {
    createPresensi,
    getPresensi,
    getMonthlyPresensi,
    getPermit,
    createIzin,
    getReportPerformance
  };
  