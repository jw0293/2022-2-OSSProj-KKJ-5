export interface ICreateRoomProps {
  imgForm: FormData;
}
export interface IFormProps {
  name: string;
  img: string;
}
export interface IRoomProps {
  name: string;
  roomId: number;
  image: string;
}

export interface IChatDetail {
  type: string;
  roomId: string;
  sender: string;
  message: string;
}
