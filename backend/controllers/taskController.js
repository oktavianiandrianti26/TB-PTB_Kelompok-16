const responseView = require('../views/responseView');
const path = require('path');
const fs = require('fs');
const moment = require('moment');
const taskModel = require('../models/taskModel');
const multer = require('multer');


  exports.getDailyTask = async (req, res) => {

    try {
        const dailyTask = await taskModel.getDailyTask();
  
        responseView.success(res, 'Get Daily Tasksuccessfully', dailyTask);
      } catch (err) {
        responseView.error(res, 'Error submitting presensi', err.message);
      }

  }


  const storage = multer.diskStorage({
    destination: function (req, file, cb) {
      cb(null, './uploads/'); // Direktori penyimpanan file
    },
    filename: function (req, file, cb) {
      cb(null, Date.now() + path.extname(file.originalname)); // Nama file unik
    },
  });
  
  const fileFilter = (req, file, cb) => {
    const fileTypes = /jpeg|jpg|png/;
    const extname = fileTypes.test(path.extname(file.originalname).toLowerCase());
    const mimeType = fileTypes.test(file.mimetype);
  
    if (extname && mimeType) {
      return cb(null, true);
    } else {
      return cb(new Error('Only .jpeg, .jpg and .png files are allowed'), false);
    }
  };
  
  const upload = multer({
    storage: storage,
    fileFilter: fileFilter,
  }).fields([
    { name: 'image1', maxCount: 1 },
    { name: 'image2', maxCount: 1 },
  ]); // Definisikan field untuk image1 dan image2
  
  exports.createTask = (req, res) => {
    upload(req, res, async (err) => {
      if (err) {
        console.log('Error uploading file:', err.message);
        return responseView.error(res, 'Error uploading file', err.message);
      }
  
      const { userId ,taskId} = req.body;
    //   console.log(taskId)
      const image1 = req.files.image1 ? req.files.image1[0].filename : null; // Ambil file dari image1
      const image2 = req.files.image2 ? req.files.image2[0].filename : null; // Ambil file dari image2
  
      // Validasi: Pastikan semua field ada
      if (!userId || !taskId) {
        console.log('Missing fields:', { userId });
        return responseView.badRequest(res, 'All fields and both images are required');
      }
  
      try {
        const datetime = moment().format('YYYY-MM-DD');
        // Proses untuk menyimpan data presensi
        const taskData = await taskModel.createTask({
            userId,
            taskId,
          image1,
          image2,
          datetime
        });
  
        // Kirim response sukses
        responseView.success(res, 'Task submitted successfully', taskData);
      } catch (err) {
        console.log('Error saving Task:', err.message);
        responseView.error(res, 'Error submitting Task', err.message);
      }
    });
  };