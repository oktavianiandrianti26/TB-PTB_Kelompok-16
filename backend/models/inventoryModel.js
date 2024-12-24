const db = require('../config/db');  

const getDailyTask = async () => {
    const result = await db.query('SELECT * FROM master_task');
    return result.rows;
  };


//   const createTask = async ({ userId, image1, image2,datetime}) => {
//     try {
//       const [date, time] = datetime.split('T');
//       const alreadySubmitTask = `SELECT * from daily_task where user_id = $1 and TO_CHAR(datetime, 'YYYY-MM-DD') = $2 LIMIT 2`;
  
//       const resultCheck = await db.query(alreadySubmitTask, [userId, date]);
  
//       if (resultCheck.rows.length > 2) {
//           throw new Error('You have already Submit Task!!');
//       }
  
//       const query = `
//         INSERT INTO daily_task (user_id, image1,image2)
//         VALUES ($1, $2, $3)
//         RETURNING id,user_id,image1,image2,datetime;
//       `;
//       const values = [userId, image1, image2];
  
//       const result = await db.query(query, values);
//       return result.rows[0]; 
//     } catch (err) {
//       throw new Error('Error inserting task: ' + err.message);
//     }
//   };

const createInventory = async ({  name,
    datetime,
    description,
    type,
    userId,
    image1,
    image2}) => {
    try {
        console.log(datetime);
    //   const [date, time] = datetime.split('T');
  
  
      const query = `
        INSERT INTO inventory (name, datetime, description, type, user_id, image1, image2)
        VALUES ($1, $2, $3, $4, $5, $6, $7 )
        RETURNING id, name, datetime, description, image1,image2, created_at;
      `;
      const values = [ name,
        datetime,
        description,
        type,
        userId,
        image1,
        image2];
  
      const result = await db.query(query, values);
      return result.rows[0];  // Return the inserted record
    } catch (err) {
      throw new Error('Error inserting presensi: ' + err.message);
    }
  };


  module.exports = {
    getDailyTask,
    createInventory
  };
  

  