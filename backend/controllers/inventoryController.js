const responseView = require('../views/responseView');
const path = require('path');
const fs = require('fs');
const moment = require('moment');
const inventoryModel = require('../models/inventoryModel');
const multer = require('multer');


  exports.getDailyTask = async (req, res) => {

    try {
        const dailyTask = await inventoryModel.getDailyTask();
  
        responseView.success(res, 'Get inventory successfully', dailyTask);
      } catch (err) {
        responseView.error(res, 'Error submitting inventory', err.message);
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
  ]);


exports.createInventory = (req, res) => {
  console.log(req.body); 

  upload(req, res, async (err) => {
    if (err) {
      console.log('Error uploading file:', err.message);
      return responseView.error(res, 'Error uploading file', err.message);
    }

    const { name, datetime, description, type, userId } = req.body;
    const image1 = req.files.image1 ? req.files.image1[0].filename : null; 
    const image2 = req.files.image2 ? req.files.image2[0].filename : null; 
    console.log(req.body);
    if (!name || !datetime  || !description  || !type || !userId) {
      console.log('Missing fields:', { name, datetime, location, description, type, userId });
      return responseView.badRequest(res, 'All fields are required');
    }

    try {
      const inventoryData = await inventoryModel.createInventory({
        name,
        datetime,
        description,
        type,
        userId,
        image1,
        image2,
      });

      responseView.success(res, 'inventory submitted successfully', inventoryData);
    } catch (err) {
      console.log('Error saving inventory:', err.message);
      responseView.error(res, 'Error submitting inventory', err.message); 
    }
  });
};