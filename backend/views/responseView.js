exports.success = (res, message, data) => {
  res.status(200).json({ status: 'success', message, data });
};

exports.badRequest = (res, message) => {
  res.status(400).json({ status: 'fail', message });
};

exports.error = (res, message, error) => {
  res.status(500).json({ status: 'error', message, error });
};