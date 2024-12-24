const db = require('../config/db');  

const getDailyTask = async () => {
    const result = await db.query('SELECT * FROM master_task');
    return result.rows;
  };


  const createTask = async ({ userId, taskId, image1, image2,datetime}) => {
    try {
      const [date, time] = datetime.split('T');
      const alreadySubmitTask = `SELECT * from daily_task where user_id = $1 and task_id = $3 and TO_CHAR(datetime, 'YYYY-MM-DD') = $2 LIMIT 1`;
  
      const resultCheck = await db.query(alreadySubmitTask, [userId, date, taskId]);
  
      console.log(datetime);
      if (resultCheck.rows.length > 0) {
          throw new Error('You have already Submit Task!!');
      }
  
      const query = `
        INSERT INTO daily_task (user_id,task_id, image1,image2)
        VALUES ($1, $2, $3, $4)
        RETURNING id,user_id,image1,image2,datetime;
      `;
      const values = [userId, taskId, image1, image2];
  
      const result = await db.query(query, values);
      return result.rows[0]; 
    } catch (err) {
      throw new Error('Error inserting task: ' + err.message);
    }
  };


  module.exports = {
    getDailyTask,
    createTask
  };
  

  