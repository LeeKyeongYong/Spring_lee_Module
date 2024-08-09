import formidable from 'formidable';
import path from 'path';

export const config = {
  api: {
    bodyParser: false,
  },
};

export default async (req, res) => {
  const form = new formidable.IncomingForm({
    uploadDir: path.join(process.cwd(), 'public/images'),
    keepExtensions: true,
  });

  form.parse(req, (err, fields, files) => {
    if (err) {
      return res.status(500).json({ error: 'File upload failed' });
    }

    const fileUrl = `/images/${path.basename(files.image[0].filepath)}`;
    res.status(200).json({ fileUrl });
  });
};
