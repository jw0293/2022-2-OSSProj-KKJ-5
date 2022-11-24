import { useState } from "react";

export const useHandleImage = () => {
  const [fileImage, setFileImage] = useState<File>();
  const [imageUrl, setImageUrl] = useState("");

  const saveFileImage = (event: React.ChangeEvent<HTMLInputElement>) => {
    // @ts-ignore
    setImageUrl(URL.createObjectURL(event.target.files[0]));
    const target = event.currentTarget;
    const files = (target.files as FileList)[0];
    if (files === undefined) {
      return;
    }
    setFileImage(files);
  };

  const deleteFileImage = () => {
    // URL.revokeObjectURL(fileImage);
    // setFileImage(null);
  };

  return {
    fileImage,
    imageUrl,
    saveFileImage,
    deleteFileImage,
  };
};
