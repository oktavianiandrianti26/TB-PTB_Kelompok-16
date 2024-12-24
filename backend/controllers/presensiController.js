const multer = require('multer');
const moment = require('moment');
const path = require('path');
const responseView = require('../views/responseView'); 
const presensiModel = require('../models/presensiModel'); 


// const storage = multer.diskStorage({
//   destination: (req, file, cb) => {
//     cb(null, 'uploads/'); 
//   },
//   filename: (req, file, cb) => {
//     cb(null, Date.now() + path.extname(file.originalname)); 
//   }
// });

// const upload = multer({ storage: storage }).single('image'); 
// const multer = require('multer');
// const upload = multer({ dest: 'uploads/' }).single('image'); // Ganti 'image' dengan nama field dari form HTML untuk file



// Konfigurasi penyimpanan untuk multer
const storage = multer.diskStorage({
  destination: function (req, file, cb) {
    cb(null, './uploads/'); // Tentukan folder tujuan untuk menyimpan file
  },
  filename: function (req, file, cb) {
    // Tentukan format nama file untuk menghindari nama yang sama
    cb(null, Date.now() + path.extname(file.originalname));
  },
});

// Filter untuk menerima hanya tipe file gambar
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

// Inisialisasi multer dengan konfigurasi
const upload = multer({
  storage: storage,
  fileFilter: fileFilter,
}).single('image'); // 'image' adalah nama field pada form yang mengirimkan gambar

// Endpoint untuk menangani presensi
exports.submitPresensi = (req, res) => {
  console.log(req.body); // Cek apakah req.body sudah berisi data selain file

  // Gunakan upload untuk menangani file terlebih dahulu
  upload(req, res, async (err) => {
    if (err) {
      console.log('Error uploading file:', err.message);
      return responseView.error(res, 'Error uploading file', err.message); // Kirim error jika ada masalah dengan upload
    }

    // Pastikan bahwa req.body dan req.file sudah tersedia
    const { name, datetime, location, description, type, userId } = req.body;
    const image = req.file ? req.file.filename : null; // Ambil nama file jika ada

    // Validasi: Pastikan semua field ada
    if (!name || !datetime || !location || !description || !image || !type || !userId) {
      console.log('Missing fields:', { name, datetime, location, description, image, type, userId });
      return responseView.badRequest(res, 'All fields are required');
    }

    try {
      // Proses untuk menyimpan data presensi
      const presensiData = await presensiModel.createPresensi({
        name,
        datetime,
        location,
        description,
        image,
        type,
        userId,
      });

      // Mengirim response sukses
      responseView.success(res, 'Presensi submitted successfully', presensiData);
    } catch (err) {
      console.log('Error saving presensi:', err.message);
      responseView.error(res, 'Error submitting presensi', err.message); // Kirim error jika gagal menyimpan data
    }
  });
};

exports.submitIzin =  async  (req, res) => {
  console.log(req.body); 



    const { name, datetime, description, type, userId } = req.body;

    // Validasi: Pastikan semua field ada
    if (!name || !datetime ||   !description || !type || !userId) {
      console.log('Missing fields:', { name, datetime, description, type, userId });
      return responseView.badRequest(res, 'All fields are required');
    }

    try {
      // Proses untuk menyimpan data presensi
      const presensiData = await presensiModel.createIzin({
        name,
        datetime,
        description,
        type,
        userId,
      });

      // Mengirim response sukses
      responseView.success(res, 'Presensi submitted successfully', presensiData);
    } catch (err) {
      console.log('Error saving presensi:', err.message);
      responseView.error(res, 'Error submitting presensi', err.message); // Kirim error jika gagal menyimpan data
    }
};

  exports.getPresensi = async (req, res) => {
    const {userId} = req.query;

    if (!userId) {
        return responseView.badRequest(res, 'User Id must be filled!!!');
    }

    try {
        const presensiData = await presensiModel.getPresensi({userId});
  
        responseView.success(res, 'Presensi submitted successfully', presensiData);
      } catch (err) {
        responseView.error(res, 'Error submitting presensi', err.message);
      }



  }

  exports.getPermit = async (req, res) => {
    const {userId} = req.query;

    if (!userId) {
        return responseView.badRequest(res, 'User Id must be filled!!!');
    }

    try {
        const yearMonth = moment().format('YYYY-MM');
        console.log(yearMonth);
        const presensiData = await presensiModel.getPermit({userId, date: yearMonth});
  
        responseView.success(res, 'GET PERMIT DATA', presensiData);
      } catch (err) {
        responseView.error(res, 'Error submitting presensi', err.message);
      }



  }